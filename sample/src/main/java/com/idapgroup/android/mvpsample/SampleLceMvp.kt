package com.idapgroup.android.mvpsample

import com.idapgroup.android.mvp.MvpPresenter
import com.idapgroup.android.mvp.RawLceView

interface SampleLceMvp {

    interface Presenter : MvpPresenter<View>

    interface View : RawLceView
}