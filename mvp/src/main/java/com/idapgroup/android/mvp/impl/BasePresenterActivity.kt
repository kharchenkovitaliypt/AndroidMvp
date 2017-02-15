package com.idapgroup.android.mvp

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v7.app.AppCompatActivity
import com.idapgroup.android.mvp.impl.PresenterDelegate

abstract class BasePresenterActivity<V, out P : MvpPresenter<V>> : AppCompatActivity() {

    private lateinit var presenterDelegate: PresenterDelegate<V, P>

    val presenter: P
        get() = presenterDelegate.presenter

    /**
     * Creates a Presenter when needed.
     * This instance should not contain explicit or implicit reference for [android.app.Activity] context
     * since it will be keep on rotations.
     */
    abstract fun createPresenter(): P

    /** Override in case of activity not implementing Presenter<View> interface <View> */
    @Suppress("UNCHECKED_CAST")
    open val presenterView: V
        get() = this as V

    /** Indicates to retain or not presenter when activity configuration changing */
    open var retainPresenter = false

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        @Suppress("UNCHECKED_CAST")
        val pd = lastCustomNonConfigurationInstance as? PresenterDelegate<V, P>
        // Create new if null
        presenterDelegate = pd ?: PresenterDelegate(createPresenter())

        if(savedInstanceState != null) {
            presenterDelegate.onRestoreState(savedInstanceState)
        }
        presenterDelegate.onCreate()
    }

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        presenterDelegate.onSaveState(outState)
    }

    @CallSuper
    override fun onRetainCustomNonConfigurationInstance(): Any? {
        return if(retainPresenter) presenterDelegate else null
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
}
