package com.idapgroup.android.rx_mvp

import android.os.Bundle
import android.support.annotation.CallSuper
import android.util.Log
import com.idapgroup.android.mvp.impl.ExtBasePresenter
import io.reactivex.*
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.internal.functions.Functions
import java.util.*
import java.util.concurrent.ConcurrentHashMap

open class RxBasePresenter<V> : ExtBasePresenter<V>() {

    // Optimization for safe subscribe
    private val ERROR_CONSUMER: (Throwable) -> Unit = { Functions.ERROR_CONSUMER.accept(it) }
    private val EMPTY_ACTION: () -> Unit = {}

    private val activeTasks = LinkedHashMap<String, Disposable>()
    private val resetTaskStateActionMap = ConcurrentHashMap<String, () -> Unit>()

    @CallSuper
    override fun onSaveState(savedState: Bundle) {
        super.onSaveState(savedState)
        savedState.putStringArrayList("task_keys", ArrayList(activeTasks.keys))
    }

    @CallSuper
    override fun onRestoreState(savedState: Bundle) {
        super.onRestoreState(savedState)
        val taskKeys = savedState.getStringArrayList("task_keys")
        taskKeys.forEach { key ->
            val activeTask = activeTasks.containsKey(key)
            if (!activeTask) resetTaskState(key)
        }
    }

    /** Preserves link for task by key while it's running  */
    protected fun <T> taskTracker(key: String): ObservableTransformer<T, T> {
        return ObservableTransformer {
                    it.doOnSubscribe { addDisposable(key, it) }
                    .doOnDispose { activeTasks.remove(key) }
        }
    }

    /** Preserves link for task by key while it's running  */
    protected fun <T> singleTaskTracker(key: String): SingleTransformer<T, T> {
        return SingleTransformer {
                    it.doOnSubscribe { addDisposable(key, it) }
                    .doOnSuccess { activeTasks.remove(key) }
                    .doOnError { activeTasks.remove(key) }
                    .doOnDispose { activeTasks.remove(key) }
        }
    }

    /** Preserves link for task by key while it's running  */
    protected fun completableTaskTracker(key: String): CompletableTransformer {
        return CompletableTransformer {
                    it.doOnSubscribe { addDisposable(key, it) }
                    .doOnTerminate { activeTasks.remove(key) }
                    .doOnDispose { activeTasks.remove(key) }
        }
    }

    private fun addDisposable(key: String, disposable: Disposable) {
        if (activeTasks.containsKey(key)) {
            throw IllegalArgumentException("Task already started")
        }
        activeTasks.put(key, disposable)
    }

    protected fun setResetTaskStateAction(key: String, resetAction: () -> Unit) {
        resetTaskStateActionMap.put(key, resetAction)
    }

    protected fun cancelTask(taskKey: String) {
        val task = activeTasks[taskKey]
        if (task != null) {
            task.dispose()
            activeTasks.remove(taskKey)
            resetTaskState(taskKey)
        }
    }

    protected fun isTaskActive(taskKey: String): Boolean {
        val task = activeTasks[taskKey]
        return task != null && !task.isDisposed
    }

    /** Calls preliminarily set a reset task state action   */
    private fun resetTaskState(taskKey: String) {
        val resetTaskAction = resetTaskStateActionMap[taskKey]
        if (resetTaskAction == null) {
            Log.w(javaClass.simpleName, "Reset task action is not set for task key: " + taskKey)
        } else {
            execute(resetTaskAction)
        }
    }

    fun <T> Observable<T>.safeSubscribe(
            onNext: (T) -> Unit,
            onError: (Throwable) -> Unit = ERROR_CONSUMER,
            onComplete: () -> Unit = EMPTY_ACTION
    ): Disposable {
        val safeOnError = { error: Throwable -> execute { onError(error) } }
        val safeOnComplete = { execute(onComplete) }
        return subscribe(
                { nextItem -> execute { onNext(nextItem) } },
                if(onError == ERROR_CONSUMER) ERROR_CONSUMER else safeOnError,
                if(onComplete == EMPTY_ACTION) EMPTY_ACTION else safeOnComplete
        )
    }

    fun <T> Single<T>.safeSubscribe(
            onSuccess: (T) -> Unit,
            onError: (Throwable) -> Unit = ERROR_CONSUMER
    ): Disposable {
        val safeOnError = { error: Throwable -> execute { onError(error) } }
        return subscribe(
                { result -> execute { onSuccess(result) } },
                if(onError == ERROR_CONSUMER) ERROR_CONSUMER else safeOnError
        )
    }

    fun Completable.safeSubscribe(
            onComplete: () -> Unit,
            onError: (Throwable) -> Unit = ERROR_CONSUMER
    ): Disposable {
        val safeOnError = { error: Throwable -> execute { onError(error) } }
        return subscribe(
                { execute(onComplete) },
                if(onError == ERROR_CONSUMER) ERROR_CONSUMER else safeOnError
        )
    }
}
