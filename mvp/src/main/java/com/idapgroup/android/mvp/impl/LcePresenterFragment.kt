package com.idapgroup.android.mvp.impl

import android.os.Bundle
import android.support.annotation.CallSuper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.idapgroup.android.mvp.impl.BasePresenterFragment
import com.idapgroup.android.mvp.LceView
import com.idapgroup.android.mvp.MvpPresenter
import com.idapgroup.android.mvp.R
import com.idapgroup.android.mvp.impl.DefaultLceComponentViewCreator
import com.idapgroup.android.mvp.impl.LceViewHandler

/** Fragment for displaying loading states(load, content, error)  */
abstract class LcePresenterFragment<V, out P : MvpPresenter<V>> :
        BasePresenterFragment<V, P>(),
        LceView{

    private var lceViewHandler = LceViewHandler()
    private val defaultLceComponentViewCreator = DefaultLceComponentViewCreator(
            { inflater, container ->
                onCreateContentView(inflater, container)
            }
    )

    abstract fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup): View

    override fun onCreateView(inflater: LayoutInflater, rootContainer: ViewGroup?, savedInstanceState: Bundle?): View {
        return lceViewHandler.createAndInitView(inflater, rootContainer, defaultLceComponentViewCreator)
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
