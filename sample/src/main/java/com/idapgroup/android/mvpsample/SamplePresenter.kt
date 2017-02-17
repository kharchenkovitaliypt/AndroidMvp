package com.idapgroup.android.mvpsample

import com.idapgroup.android.rx_mvp.RxBasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

open class SamplePresenter : SampleMvp.Presenter, RxBasePresenter<SampleMvp.View>() {

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
                .safeSubscribe(
                        {
                            view!!.hideLoad()
                            view!!.goToMain()
                        },
                        { view!!.showError(it) }
                )
    }

    override fun onAsk(question: String) {
        view!!.showMessage("- $question \n - Good question. There is something to think about")
    }
}