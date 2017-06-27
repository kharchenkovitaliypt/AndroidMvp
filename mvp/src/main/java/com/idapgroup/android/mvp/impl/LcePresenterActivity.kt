package com.idapgroup.android.mvp.impl

import android.os.Bundle
import android.support.annotation.CallSuper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.idapgroup.android.mvp.LceView
import com.idapgroup.android.mvp.MvpPresenter
import com.idapgroup.android.mvp.R

/** Fragment for displaying loading states(load, content, error)  */
abstract class LcePresenterActivity<V, out P : MvpPresenter<V>> :
        BasePresenterActivity<V, P>(),
        LceView {

    protected val lceViewHandler = LceViewHandler()

    open val lceViewCreator: LceViewCreator = DefaultLceViewCreator {
        inflater, container -> onCreateContentView(inflater, container)
    }

    abstract fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup): View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        super.setContentView(LceViewHandler.BASE_CONTAINER_ID)
        val lceContainer = findViewById(R.id.lce_container) as ViewGroup
        lceViewHandler.initView(layoutInflater, lceContainer, lceViewCreator)
    }

    override fun setContentView(layoutResID: Int) {
        throw IllegalStateException("Use onCreateContentView()")
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
