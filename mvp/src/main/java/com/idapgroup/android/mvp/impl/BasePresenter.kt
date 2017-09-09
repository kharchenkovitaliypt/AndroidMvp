@file:JvmName("BasePresenter")
package com.idapgroup.android.mvp.impl

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.idapgroup.android.mvp.MvpPresenter
import java.util.*

abstract class BasePresenter<V> : MvpPresenter<V> {

    private val handler = Handler(Looper.getMainLooper())
    private val pendingActions = ArrayList<Action>()

    protected var view : V? = null
        private set

    final override fun attachView(view: V) {
        this.view = view
    }

    final override fun detachView() {
        this.view = null
    }

    override fun onCreate() { }
    override fun onSaveState(savedState: Bundle) { }
    override fun onRestoreState(savedState: Bundle) { }

    override fun onAttachedView(view: V) {
        pendingActions.forEach { it() }
        pendingActions.clear()
    }
    override fun onDetachedView() { }

    /** Calls this action immediately if view attached
     * or postpone for the moment it will be attached  */
    protected fun execute(action: Action) {
        if(Looper.myLooper() == Looper.getMainLooper()) {
            innerExecute(action)
        } else {
            handler.post { innerExecute(action) }
        }
    }

    private fun innerExecute(action: Action) {
        if (view != null) {
            action()
        } else {
            pendingActions.add(action)
        }
    }
}
