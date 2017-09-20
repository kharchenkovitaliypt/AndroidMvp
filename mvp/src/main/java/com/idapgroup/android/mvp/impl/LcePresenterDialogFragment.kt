package com.idapgroup.android.mvp.impl

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.idapgroup.android.mvp.LceView
import com.idapgroup.android.mvp.MvpPresenter

/** Fragment for displaying loading states(load, content, error)  */
abstract class LcePresenterDialogFragment<V, out P : MvpPresenter<V>> :
        BasePresenterDialogFragment<V, P>(),
        LceView{

    protected val lceViewHandler = LceViewHandler()

    open val lceViewCreator: LceViewCreator = SimpleLceViewCreator({ inflater, container ->
        onCreateContentView(inflater, container)
    })

    abstract fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup): View

    override fun onCreateView(inflater: LayoutInflater, rootContainer: ViewGroup?, savedInstanceState: Bundle?): View {
        return lceViewHandler.createAndInitView(inflater, rootContainer, lceViewCreator)
    }

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
