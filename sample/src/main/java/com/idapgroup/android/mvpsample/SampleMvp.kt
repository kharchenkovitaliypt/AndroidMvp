package com.idapgroup.android.mvpsample

import com.idapgroup.android.mvp.LoadView
import com.idapgroup.android.mvp.MvpPresenter
import com.idapgroup.android.mvp.RawErrorView

interface SampleMvp {

    interface Presenter : MvpPresenter<View> {
        fun onAsk(question: String)
        fun onConfirm()
    }

    interface View : LoadView, RawErrorView {
        fun showMessage(message: String)
        fun goToMain()
    }
}