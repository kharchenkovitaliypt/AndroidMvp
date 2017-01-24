package com.idapgroup.android.mvp.impl

import android.os.Bundle
import android.support.annotation.CallSuper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.idapgroup.android.mvp.BasePresenterFragment
import com.idapgroup.android.mvp.LceView
import com.idapgroup.android.mvp.MvpPresenter
import com.idapgroup.android.mvp.R
import com.idapgroup.android.rx_mvp.load.LceViewHandler

/** Fragment for displaying loading states(load, content, error)  */
abstract class LcePresenterFragment<V, out P : MvpPresenter<V>> :
        BasePresenterFragment<V, P>(),
        LceView,
        LceViewHandler.LceComponentViewCreator {

    private var lceViewHandler = LceViewHandler()

    override fun onCreateErrorView(inflater: LayoutInflater, container: ViewGroup) : View {
        return inflater.inflate(R.layout.lce_base_error, container, false)
    }

    override fun onCreateLoadView(inflater: LayoutInflater, container: ViewGroup) : View {
        return inflater.inflate(R.layout.lce_base_load, container, false)
    }

    override fun onCreateView(inflater: LayoutInflater, rootContainer: ViewGroup?, savedInstanceState: Bundle?): View {
        return lceViewHandler.createAndInitView(inflater, rootContainer, this)
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        lceViewHandler.resetView()
    }

    override fun showLoad() {
        lceViewHandler.showLoad()
    }

    override fun showContent() {
        lceViewHandler.showContent()
    }

    override fun showError(errorMessage: String, retry : (() -> Unit)?) {
        lceViewHandler.showError(errorMessage, retry)
    }
}
