package com.idapgroup.android.mvp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.SparseArray

/** Temporary preserves [PresenterDelegate]s when activity configuration changing(recreating) */
private val tmpPresenterDelegatesStorage = SparseArray<PresenterDelegate<*, *>>()

abstract class BasePresenterFragment<V, out P : MvpPresenter<V>> : Fragment() {
    val KEY_FRAGMENT_ID = "fragment_id"

    private lateinit var presenterDelegate: PresenterDelegate<V, P>

    /**
     * Creates a Presenter when needed.
     * This instance should not contain explicit or implicit reference for [android.app.Activity] context
     * since it will be keep on rotations.
     */
    abstract fun createPresenter(): P

    fun getPresenter() = presenterDelegate.presenter

    /** Override in case of activity not implementing Presenter<View> interface <View> */
    @Suppress("UNCHECKED_CAST")
    open val presenterView: V
        get() = this as V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(savedInstanceState == null || tmpPresenterDelegatesStorage.size() == 0) {
            presenterDelegate = PresenterDelegate(createPresenter())
        } else {
            // Restore previously preserved presenter when configuration change
            val fragmentId = savedInstanceState.getInt(KEY_FRAGMENT_ID)
            @Suppress("UNCHECKED_CAST")
            presenterDelegate = tmpPresenterDelegatesStorage.get(fragmentId) as PresenterDelegate<V, P>
            tmpPresenterDelegatesStorage.delete(fragmentId)
        }
    }

    override fun onSaveInstanceState(savedState: Bundle) {
        super.onSaveInstanceState(savedState)
        presenterDelegate.onSaveState(savedState)
        // Tmp preserve presenter when configuration change
        savedState.putInt(KEY_FRAGMENT_ID, getFragmentId())
        tmpPresenterDelegatesStorage.append(getFragmentId(), presenterDelegate)
    }

    override fun onResume() {
        super.onResume()
        presenterDelegate.attachView(presenterView)
    }

    override fun onPause() {
        presenterDelegate.detachView()
        super.onPause()
    }

    private fun getFragmentId() : Int = hashCode()
}
