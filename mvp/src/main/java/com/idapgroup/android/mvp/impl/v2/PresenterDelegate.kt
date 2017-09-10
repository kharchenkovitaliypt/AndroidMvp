package com.idapgroup.android.mvp.impl.v2

import android.os.Bundle
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

internal class PresenterDelegateImpl<in V, out P : MvpPresenter<V>>(
        createPresenter : () -> P,
        private val retain: Retain? = null,
        private val retainId: String? = null
) : PresenterDelegate<V, P> {

    override val presenter: P
    var created = false

    init {
        val savedFragmentId = retain?.savedState?.getString(KEY_RETAINED_ID)
        if (savedFragmentId != null && retainedPresenters.isNotEmpty()) {
            // Restore previously preserved presenter when configuration changed
            @Suppress("UNCHECKED_CAST")
            presenter = retainedPresenters[savedFragmentId] as P
            retainedPresenters.remove(savedFragmentId)
        } else {
            presenter = createPresenter()
        }
        retain?.savedState?.let { onRestoreState(it) }
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
        if(retain != null) {
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
        presenter.attachView(view)
        presenter.onAttachedView(view)
    }

    override fun detachView() {
        presenter.onDetachedView()
        presenter.detachView()
    }
}
