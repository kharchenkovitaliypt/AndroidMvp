package com.idapgroup.android.mvp.impl

import android.os.Handler
import android.os.Looper
import android.support.annotation.CallSuper
import java.util.*

open class ExtBasePresenter<V> : BasePresenter<V>() {

    private val handler = Handler(Looper.getMainLooper())
    private val pendingActions = ArrayList<() -> Unit>()

    @CallSuper
    override fun onAttachedView(view: V) {
        super.onAttachedView(view)
        pendingActions.forEach { it() }
        pendingActions.clear()
    }

    /** Calls this action immediately if rootView attached
     * or postpone for the moment it will be attached  */
    protected fun execute(action: () -> Unit) {
        if(Looper.myLooper() == Looper.getMainLooper()) {
            innerExecute(action)
        } else {
            handler.post { innerExecute(action) }
        }
    }

    private fun innerExecute(action: () -> Unit) {
        if (view != null) {
            action()
        } else {
            pendingActions.add(action)
        }
    }
}
