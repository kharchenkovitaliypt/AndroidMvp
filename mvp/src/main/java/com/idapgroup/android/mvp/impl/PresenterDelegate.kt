package com.idapgroup.android.mvp

import android.os.Bundle

class PresenterDelegate<V, out P : MvpPresenter<V>>(val presenter : P) {

    var created = false

    fun onCreate() {
        if(!created) {
            presenter.onCreate()
            created = true
        }
    }

    fun onSaveState(savedState: Bundle) {
        presenter.onSaveState(savedState)
        savedState.putBoolean("key_delegate_created", created)
    }

    fun onRestoreState(savedState: Bundle) {
        created = savedState.getBoolean("key_delegate_created")
        presenter.onRestoreState(savedState)
    }

    fun attachView(view: V) {
        presenter.attachView(view)
        presenter.onAttachedView(view)
    }

    fun detachView() {
        presenter.detachView()
        presenter.onDetachedView()
    }
}
