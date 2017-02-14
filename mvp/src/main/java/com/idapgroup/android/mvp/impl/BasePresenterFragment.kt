package com.idapgroup.android.mvp

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.Fragment
import android.util.SparseArray
import java.util.*

/** Temporary preserves [PresenterDelegate]s when activity configuration changing(recreating) */
val tmpPresenterDelegatesStorage = LinkedHashMap<String, PresenterDelegate<*, *>>()

abstract class BasePresenterFragment<V, out P : MvpPresenter<V>> : Fragment() {
    val KEY_FRAGMENT_ID = "fragment_id"

    private lateinit var presenterDelegate: PresenterDelegate<V, P>

    /**
     * Creates a Presenter when needed.
     * This instance should not contain explicit or implicit reference for [android.app.Activity] context
     * since it will be keep on rotations.
     */
    abstract fun onCreatePresenter(): P

    val presenter: P
        get() = presenterDelegate.presenter

    /** Override in case of activity not implementing Presenter<View> interface <View> */
    @Suppress("UNCHECKED_CAST")
    open val presenterView: V
        get() = this as V

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(savedInstanceState == null || tmpPresenterDelegatesStorage.size == 0) {
            presenterDelegate = PresenterDelegate(onCreatePresenter())
        } else {
            // Restore previously preserved presenter when configuration change
            val fragmentId = savedInstanceState.getString(KEY_FRAGMENT_ID)
            @Suppress("UNCHECKED_CAST")
            presenterDelegate = tmpPresenterDelegatesStorage[fragmentId] as PresenterDelegate<V, P>
            tmpPresenterDelegatesStorage.remove(fragmentId)
        }
        presenterDelegate.onCreate()
    }

    @CallSuper
    override fun onSaveInstanceState(savedState: Bundle) {
        super.onSaveInstanceState(savedState)
        presenterDelegate.onSaveState(savedState)
        // Tmp preserve presenter when configuration change
        val fragmentId = javaClass.name + getFragmentId()
        savedState.putString(KEY_FRAGMENT_ID, fragmentId)
        tmpPresenterDelegatesStorage.put(fragmentId, presenterDelegate)
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        presenterDelegate.attachView(presenterView)
    }

    @CallSuper
    override fun onPause() {
        presenterDelegate.detachView()
        super.onPause()
    }

    /**
     * Override if at the same time may exist many examples of the same class.
     * May be unique only for this class of fragment not required global uniqueness
     * */
    protected open fun getFragmentId() : String = hashCode().toString()
}
