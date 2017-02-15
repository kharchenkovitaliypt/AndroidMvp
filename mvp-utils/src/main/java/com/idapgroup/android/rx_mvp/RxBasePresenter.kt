package com.idapgroup.android.rx_mvp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.annotation.CallSuper
import android.util.Log
import com.idapgroup.android.mvp.impl.BasePresenter
import com.idapgroup.android.mvp.impl.ExtBasePresenter
import io.reactivex.ObservableTransformer
import io.reactivex.SingleTransformer
import io.reactivex.disposables.Disposable
import java.util.*
import java.util.concurrent.ConcurrentHashMap

open class RxBasePresenter<V> : ExtBasePresenter<V>() {

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
            it.doOnSubscribe({ disposable ->
                if (activeTasks.containsKey(key)) {
                    throw IllegalArgumentException("Task already started")
                }
                activeTasks.put(key, disposable)
            })
            .doOnTerminate({ activeTasks.remove(key) })
        }
    }

    /** Preserves link for task by key while it's running  */
    protected fun <T> singleTaskTracker(key: String): SingleTransformer<T, T> {
        return SingleTransformer {
                    it.doOnSubscribe({ disposable ->
                        if (activeTasks.containsKey(key)) {
                            throw IllegalArgumentException("Task already started")
                        }
                        activeTasks.put(key, disposable)
                    })
                    .doOnSuccess({ activeTasks.remove(key) })
                    .doOnError({ activeTasks.remove(key) })
                    .doOnDispose({ activeTasks.remove(key) })
        }
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
}
