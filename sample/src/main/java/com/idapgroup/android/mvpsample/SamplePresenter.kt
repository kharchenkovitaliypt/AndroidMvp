package com.idapgroup.android.mvpsample

import android.os.Bundle
import android.util.Log
import com.idapgroup.android.rx_mvp.RxBasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

private val TAG = "SamplePresenter"

open class SamplePresenter : SampleMvp.Presenter, RxBasePresenter<SampleMvp.View>() {

    init {
        setResetTaskStateAction("task_confirm", { view!!.hideLoad() })
    }

    override fun onDetachedView() {
        super.onDetachedView()
        Log.d(TAG, "onDetachView()")
    }

    override fun onSaveState(savedState: Bundle) {
        super.onSaveState(savedState)
        Log.d(TAG, "onSaveState()")
    }

    override fun onConfirm() {
        view!!.showLoad()
        Observable
                .fromCallable { Thread.sleep(3000) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .taskTracker("task_confirm")
                .safeSubscribe({
                    view!!.hideLoad()
                    view!!.goToMain()
                }, {
                    view!!.showError(it)
                })
    }

    override fun takeUntilDetachView() {
        val tag = "onDetachView"
        Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { Log.d(TAG, "takeUntilDetachView() tag: $tag, doFinally") }
                .cancelOnDetachView()
                .safeSubscribe({
                    Log.d(TAG, "takeUntilDetachView() tag: $tag, onNext: $it")
                    view!!.showMessage("Tick: $it")
                }, {
                    Log.d(TAG, "takeUntilDetachView() tag: $tag, onError: $it")
                    view!!.showError(it)
                }, {
                    Log.d(TAG, "takeUntilDetachView() tag: $tag, onComplete")
                })
    }

    override fun onAsk(question: String) {
        view!!.showMessage("- $question \n - Good question. There is something to think about")
    }
}