package com.idapgroup.android.mvp.impl.v2

import android.os.Bundle
import android.util.Log
import com.idapgroup.android.mvp.MvpPresenter
import java.util.*

private val KEY_RETAINED_ID = "retained_id"
private val KEY_DELEGATE_CREATED = "key_delegate_created"
internal val retainedPresenters = LinkedHashMap<String, MvpPresenter<*>>()

interface PresenterDelegate<in V, out P : MvpPresenter<V>> {
    val presenter: P

    fun attachView(view: V)
    fun detachView()
}

internal class PresenterDelegateImpl<V, out P : MvpPresenter<V>>(
        createPresenter : () -> P,
        savedState: Bundle?,
        private val retain: Boolean = false,
        private val retainId: String? = null
) : PresenterDelegate<V, P> {

    override val presenter: P
    var created = false
    var view: V? = null

    init {
        val savedFragmentId = savedState?.getString(KEY_RETAINED_ID)
        if (savedFragmentId != null && retainedPresenters.isNotEmpty()) {
            // Restore previously preserved presenter when configuration changed
            @Suppress("UNCHECKED_CAST")
            presenter = retainedPresenters[savedFragmentId] as P
            retainedPresenters.remove(savedFragmentId)
        } else {
            presenter = createPresenter()
        }
        savedState?.let { onRestoreState(it) }
        onCreate()
    }

    private fun onCreate() {
        if(!created) {
            presenter.onCreate()
            created = true
        }
    }

    fun onSaveState(outState: Bundle) {
        presenter.onSaveState(outState)
        outState.putBoolean(KEY_DELEGATE_CREATED, created)
        if(retain) {
            // Tmp retain presenter on configuration change time
            outState.putString(KEY_RETAINED_ID, retainId!!)
            retainedPresenters.put(retainId, presenter)
        }
    }

    private fun onRestoreState(savedState: Bundle) {
        created = savedState.getBoolean(KEY_DELEGATE_CREATED)
        presenter.onRestoreState(savedState)
    }

    override fun attachView(view: V) {
        if(this.view != null) {
            if(MVP_STRICT_MODE) {
                throw IllegalStateException("${this.view} is already attached")
            } else {
                Log.e("Presenter", "MvpPresenter.attachView() ${this.view} is attached")
            }
        } else {
            this.view = view
            presenter.attachView(view)
            presenter.onAttachedView(view)
        }
    }

    override fun detachView() {
        if(view == null) {
            if(MVP_STRICT_MODE) {
                throw IllegalStateException("$view is detached")
            } else {
                Log.e("Presenter", "MvpPresenter.detachView() $view is detached")
            }
        } else {
            presenter.onDetachedView()
            presenter.detachView()
            this.view = null
        }
    }
}
