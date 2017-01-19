package com.idapgroup.android.mvpsample

import com.idapgroup.android.rx_mvp.BaseRxPresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

open class SamplePresenter : SampleMvp.Presenter, BaseRxPresenter<SampleMvp.View>() {

    init {
        setResetTaskStateAction("task_confirm", { view!!.hideLoad() })
    }

    override fun onConfirm() {
        view!!.showLoad()
        Observable
                .fromCallable { Thread.sleep(3000) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(taskTracker("task_confirm"))
                .subscribe(
                        {
                            execute {
                                view!!.hideLoad()
                                view!!.goToMain()
                            }
                        },
                        { execute { view!!.showError(it) } }
                )
    }

    override fun onAsk(question: String) {
        view!!.showMessage("- $question \n - Good question. There is something to think about")
    }
}