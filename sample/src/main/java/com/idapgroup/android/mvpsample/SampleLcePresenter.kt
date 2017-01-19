package com.idapgroup.android.mvpsample

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.util.*

class SampleLcePresenter : SampleLceMvp.Presenter, SamplePresenter() {

    init {
        setResetTaskStateAction("task_start", { onStart() })
    }

    override fun onStart() {
        (view as SampleLceMvp.View).showLceLoad()
        Observable
                .fromCallable {
                    if(Random().nextBoolean()) {
                        Thread.sleep(1000)
                        throw IOException("OH MY GOD. We have a big problem!!!")
                    } else {
                        Thread.sleep(4000)
                    }
                }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(taskTracker("task_start"))
                .subscribe(
                        { execute { (view as SampleLceMvp.View).showContent() } },
                        { execute { view!!.showError(it) } }
                )
    }

    override fun onRetry() {
        onStart()
    }
}