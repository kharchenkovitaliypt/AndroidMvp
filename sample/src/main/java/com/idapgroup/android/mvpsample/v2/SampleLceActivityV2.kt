package com.idapgroup.android.mvpsample.v2

import android.os.Bundle
import com.idapgroup.android.mvp.RawLceView
import com.idapgroup.android.mvp.impl.LceViewHandler
import com.idapgroup.android.mvp.impl.createLceViewCreator
import com.idapgroup.android.mvp.impl.setContentView
import com.idapgroup.android.mvp.impl.v2.Retain
import com.idapgroup.android.mvp.impl.v2.attachPresenter
import com.idapgroup.android.mvpsample.R
import com.idapgroup.android.mvpsample.SampleLceMvp
import com.idapgroup.android.mvpsample.SampleLcePresenter

class SampleLceActivityV2(
        val lceViewHandler: LceViewHandler = LceViewHandler()
) : SampleActivityV2(), SampleLceMvp.View, RawLceView by lceViewHandler {

    private lateinit var presenter: SampleLceMvp.Presenter

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        presenter = attachPresenter(this, this, ::SampleLcePresenter, Retain(savedState))
    }

    override fun onInitView() {
        setContentView(lceViewHandler, createLceViewCreator(R.layout.screen_sample))
    }
}