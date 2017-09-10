@file:JvmName("PresenterHandler")
package com.idapgroup.android.mvp.impl.v2

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.View
import com.idapgroup.android.mvp.MvpPresenter
import com.idapgroup.android.mvp.impl.v2.LifecyclePair.*

enum class LifecyclePair {
    CREATE_DESTROY_VIEW, START_STOP, RESUME_PAUSE
}

@JvmOverloads
fun <V, P: MvpPresenter<V>> attachPresenter(
        activity: Activity,
        view: V,
        createPresenter: () -> P,
        retain: Boolean = false,
        savedState: Bundle? = null,
        manualHandleView: Boolean = false,
        lifecyclePair: LifecyclePair = CREATE_DESTROY_VIEW
): P {
    return attachPresenterDelegate(
            activity, view, createPresenter,
            retain, savedState,
            manualHandleView, lifecyclePair
    ).presenter
}

@JvmOverloads
fun <V, P: MvpPresenter<V>> attachPresenterDelegate(
        activity: Activity,
        view: V,
        createPresenter: () -> P,
        retained: Boolean = false,
        savedState: Bundle? = null,
        manualHandleView: Boolean = false,
        lifecyclePair: LifecyclePair = CREATE_DESTROY_VIEW
): PresenterDelegate<V, P> {

    val retainedId: String = activity.javaClass.name + activity.hashCode()
    val delegate = PresenterDelegateImpl(createPresenter, retained, savedState, retainedId)
    if(!manualHandleView && lifecyclePair === CREATE_DESTROY_VIEW) {
        delegate.attachView(view)
    }
    val lifecycleCallbacks = object : ActivityLifecycleCallbacks {

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

        override fun onActivitySaveInstanceState(a: Activity, outState: Bundle) {
            delegate.onSaveState(outState)
        }
        override fun onActivityStarted(a: Activity) {
            if(!manualHandleView && lifecyclePair === START_STOP) {
                delegate.attachView(view)
            }
        }
        override fun onActivityResumed(a: Activity) {
            if(!manualHandleView && lifecyclePair === RESUME_PAUSE) {
                delegate.attachView(view)
            }
        }
        override fun onActivityPaused(a: Activity) {
            if(!manualHandleView && lifecyclePair === RESUME_PAUSE) {
                delegate.detachView()
            }
        }
        override fun onActivityStopped(a: Activity) {
            if(!manualHandleView && lifecyclePair === START_STOP) {
                delegate.detachView()
            }
        }
        override fun onActivityDestroyed(a: Activity) {
            if(!manualHandleView && lifecyclePair === CREATE_DESTROY_VIEW) {
                delegate.detachView()
            }
        }
    }
    activity.application.registerActivityLifecycleCallbacks(lifecycleCallbacks.filter(activity))
    return  PresenterDelegateChecker(delegate, manualHandleView)
}

@JvmOverloads
fun <V, P: MvpPresenter<V>> attachPresenter(
        fragment: Fragment,
        view: V,
        createPresenter: () -> P,
        retained: Boolean = false,
        savedState: Bundle? = null,
        manualHandleView: Boolean = false,
        lifecyclePair: LifecyclePair = CREATE_DESTROY_VIEW
): P {
    return attachPresenterDelegate(
            fragment, view, createPresenter,
            retained, savedState,
            manualHandleView, lifecyclePair
    ).presenter
}

@JvmOverloads
fun <V, P: MvpPresenter<V>> attachPresenterDelegate(
        fragment: Fragment,
        view: V,
        createPresenter: () -> P,
        retain: Boolean = false,
        savedState: Bundle? = null,
        manualHandleView: Boolean = false,
        lifecyclePair: LifecyclePair = CREATE_DESTROY_VIEW
): PresenterDelegate<V, P> {

    val retainedId = fragment.javaClass.name + fragment.hashCode()
    val delegate = PresenterDelegateImpl(createPresenter, retain, savedState, retainedId)
    if(!manualHandleView && lifecyclePair === CREATE_DESTROY_VIEW) {
        delegate.attachView(view)
    }
    val lifecycleCallbacks = object : FragmentManager.FragmentLifecycleCallbacks() {

        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View?, savedInstanceState: Bundle?) {}

        override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
            delegate.onSaveState(outState)
        }
        override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
            if(!manualHandleView && lifecyclePair === START_STOP) {
                delegate.attachView(view)
            }
        }
        override fun onFragmentResumed(fm: FragmentManager, f: Fragment?) {
            if(!manualHandleView && lifecyclePair === RESUME_PAUSE) {
                delegate.attachView(view)
            }
        }
        override fun onFragmentPaused(fm: FragmentManager, f: Fragment?) {
            if(!manualHandleView && lifecyclePair === RESUME_PAUSE) {
                delegate.detachView()
            }
        }
        override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
            if(!manualHandleView && lifecyclePair === START_STOP) {
                delegate.detachView()
            }
        }
        override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
            if(!manualHandleView && lifecyclePair === CREATE_DESTROY_VIEW) {
                delegate.detachView()
            }
        }
    }
    fragment.fragmentManager.registerFragmentLifecycleCallbacks(
            lifecycleCallbacks.filter(fragment), false)
    return PresenterDelegateChecker(delegate, manualHandleView)
}

internal class PresenterDelegateChecker<in V, out P: MvpPresenter<V>>(
        val delegate: PresenterDelegate<V, P>,
        val manualHandleView: Boolean
) : PresenterDelegate<V, P> by delegate {

    override fun attachView(view: V) {
        if(!manualHandleView) {
            throw IllegalStateException("Use only with manualViewAttach set to true")
        }
        delegate.attachView(view)
    }
    override fun detachView() {
        if(!manualHandleView) {
            throw IllegalStateException("Use only with manualViewAttach set to true")
        }
        delegate.detachView()
    }
}