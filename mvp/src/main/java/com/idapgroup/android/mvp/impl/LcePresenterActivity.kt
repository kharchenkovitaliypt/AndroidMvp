package com.idapgroup.android.mvp.impl

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.idapgroup.android.mvp.LceView
import com.idapgroup.android.mvp.MvpPresenter

/** Fragment for displaying loading states(load, content, error)  */
abstract class LcePresenterActivity<V, out P : MvpPresenter<V>> :
        BasePresenterActivity<V, P>(),
        LceView {

    protected val lceViewHandler = LceViewHandler()

    open val lceViewCreator: LceViewCreator = SimpleLceViewCreator({ inflater, container ->
        onCreateContentView(inflater, container)
    })

    abstract fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup): View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(lceViewHandler, lceViewCreator)
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
