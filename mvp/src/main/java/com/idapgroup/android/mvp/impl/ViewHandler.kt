package com.idapgroup.android.rx_mvp

import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.CallSuper
import android.support.annotation.IdRes
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.View.NO_ID
import android.view.ViewGroup
/** Simple view controller  */
abstract class ViewHandler(@IdRes val id: Int = NO_ID) {

    private val STATE_KEY_HIERARCHY = "savedHierarchyState"
    private val STATE_KEY_VISIBLE = "visibility"

    var rootView: View? = null
        private set

    abstract val layoutRes: Int

    /** Must be checked from the caller side before inflation (may prevent it)  */
    var visible: Boolean = true
        set(visible) {
            this.visible = visible
            rootView?.visibility = if (visible) View.VISIBLE else View.GONE
        }

    fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val rootView = inflater.inflate(layoutRes, container, false)
        // Prevent standard children saving mechanism
        rootView.isSaveFromParentEnabled = false
        rootView.visibility = if (visible) View.VISIBLE else View.GONE

        this.rootView = rootView
        onBindView(rootView)

        return rootView
    }

    fun onDestroyView() {
        rootView = null
    }

    protected fun onBindView(view: View) {}

    fun onSaveInstanceState(): Bundle {
        val savedState = Bundle()
        savedState.putBoolean(STATE_KEY_VISIBLE, visible)
        if (rootView != null) {
            val savedHierarchyState = SparseArray<Parcelable>()
            rootView!!.saveHierarchyState(savedHierarchyState)
            savedState.putSparseParcelableArray(STATE_KEY_HIERARCHY, savedHierarchyState)
        }
        return savedState
    }

    @CallSuper
    fun onRestoreInstanceState(savedState: Bundle) {
        visible = savedState.getBoolean(STATE_KEY_VISIBLE, true)
        val savedHierarchyState = savedState.getSparseParcelableArray<Parcelable>(STATE_KEY_HIERARCHY)
        if (savedHierarchyState != null && rootView != null) {
            rootView!!.restoreHierarchyState(savedHierarchyState)
        }
    }
}
