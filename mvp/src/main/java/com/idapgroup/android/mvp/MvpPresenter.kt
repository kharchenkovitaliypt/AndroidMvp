package com.idapgroup.android.mvp

import android.os.Bundle

interface MvpPresenter<in V> {

    fun attachView(view : V)
    fun detachView()
    fun onAttachedView(view: V)
    fun onDetachedView()

    @Deprecated("Don't use")
    fun onCreate()
    fun onSaveState(savedState: Bundle)
    fun onRestoreState(savedState: Bundle)
}
