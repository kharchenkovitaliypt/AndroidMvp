package com.idapgroup.android.mvp.impl

import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.IdRes
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.View.NO_ID
import android.view.ViewGroup

private val STATE_KEY_HIERARCHY = "savedHierarchyState"
private val STATE_KEY_VISIBLE = "visibility"

/** Simple view controller  */
abstract class ViewHandler(@IdRes val id: Int = NO_ID) {

    var baseView: View? = null
        private set

    abstract val layoutRes: Int

    /** Must be checked from the caller side before inflation (may prevent it)  */
    var visible: Boolean = true
        set(visible) {
            field = visible
            baseView?.visibility = if (visible) View.VISIBLE else View.GONE
        }

    fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val rootView = inflater.inflate(layoutRes, container, false)
        // Prevent standard children saving mechanism
        rootView.isSaveFromParentEnabled = false
        rootView.visibility = if (visible) View.VISIBLE else View.GONE

        this.baseView = rootView
        onBindView(rootView)
        return rootView
    }

    fun onDestroyView() {
        baseView = null
    }

    protected fun onBindView(view: View) {}

    fun onSaveInstanceState(): Bundle {
        val savedState = Bundle()
        savedState.putBoolean(STATE_KEY_VISIBLE, visible)
        if (baseView != null) {
            val savedHierarchyState = SparseArray<Parcelable>()
            baseView!!.saveHierarchyState(savedHierarchyState)
            savedState.putSparseParcelableArray(STATE_KEY_HIERARCHY, savedHierarchyState)
        }
        return savedState
    }

    fun onRestoreInstanceState(savedState: Bundle) {
        visible = savedState.getBoolean(STATE_KEY_VISIBLE, true)
        val savedHierarchyState = savedState.getSparseParcelableArray<Parcelable>(STATE_KEY_HIERARCHY)
        if (savedHierarchyState != null && baseView != null) {
            baseView!!.restoreHierarchyState(savedHierarchyState)
        }
    }
}
