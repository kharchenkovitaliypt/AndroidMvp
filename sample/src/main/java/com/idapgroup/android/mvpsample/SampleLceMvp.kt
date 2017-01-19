package com.idapgroup.android.mvpsample

import com.idapgroup.android.mvp.LceView

interface SampleLceMvp {

    interface Presenter : SampleMvp.Presenter {
        fun onRetry()
    }

    interface View : SampleMvp.View, LceView {
        fun showLceLoad()
    }
}