package com.idapgroup.android.mvp

import android.os.Bundle

class PresenterDelegate<V, out P : MvpPresenter<V>>(val presenter : P) {

    var started = false

    fun attachView(view: V) {
        presenter.attachView(view)
        presenter.onAttachedView(view)
        if(!started) {
            presenter.onStart()
            started = true
        }
    }

    fun detachView() {
        presenter.detachView()
        presenter.onDetachedView()
    }

    fun onSaveState(savedState: Bundle) {
        presenter.onSaveState(savedState)
        savedState.putBoolean("key_delegate_started", started)
    }

    fun onRestoreState(savedState: Bundle) {
        started = savedState.getBoolean("key_delegate_started")
        presenter.onRestoreState(savedState)
    }
}
