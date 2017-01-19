package com.idapgroup.android.mvp

import android.os.Bundle

abstract class BasePresenter<V> : MvpPresenter<V> {

    open var view : V? = null

    final override fun attachView(view: V) {
        this.view = view
    }

    final override fun detachView() {
        this.view = null
    }

    override fun onAttachedView(view: V) {}
    override fun onDetachedView() {}
    override fun onStart() { }
    override fun onSaveState(savedState: Bundle) { }
    override fun onRestoreState(savedState: Bundle) { }
}
