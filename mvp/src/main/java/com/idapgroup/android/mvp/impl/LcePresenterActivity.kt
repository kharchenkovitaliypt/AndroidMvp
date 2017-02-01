package com.idapgroup.android.mvp.impl

import android.os.Bundle
import android.support.annotation.CallSuper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.idapgroup.android.mvp.*
import com.idapgroup.android.rx_mvp.load.DefaultLceComponentViewCreator

import com.idapgroup.android.rx_mvp.load.LceViewHandler

/** Fragment for displaying loading states(load, content, error)  */
abstract class LcePresenterActivity<V, out P : MvpPresenter<V>> :
        BasePresenterActivity<V, P>(),
        LceView {

    private var lceViewHandler = LceViewHandler()
    private val defaultLceComponentViewCreator = DefaultLceComponentViewCreator(
        { inflater, container ->
            onCreateContentView(inflater, container)
        }
    )

    abstract fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup): View

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        super.setContentView(LceViewHandler.BASE_CONTAINER_ID)
        val lceContainer = findViewById(R.id.lce_container) as ViewGroup
        lceViewHandler.initView(layoutInflater, lceContainer, defaultLceComponentViewCreator)
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
