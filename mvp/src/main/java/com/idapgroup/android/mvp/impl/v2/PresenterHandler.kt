@file:JvmName("PresenterHandler")
package com.idapgroup.android.mvp.impl.v2

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentManager.FragmentLifecycleCallbacks
import android.view.View
import com.idapgroup.android.mvp.BuildConfig
import com.idapgroup.android.mvp.MvpPresenter
import com.idapgroup.android.mvp.impl.v2.LifecyclePair.*

var mvpStrictMode = false
var defaultLifecyclePair = LifecyclePair.START_STOP

enum class LifecyclePair {
    CREATE_DESTROY_VIEW, START_STOP, RESUME_PAUSE
}

@JvmOverloads
fun <V, P: MvpPresenter<V>> Activity.attachPresenter(
        view: V,
        createPresenter: () -> P,
        savedState: Bundle?,
        retain: Boolean = false,
        manualHandleView: Boolean = false,
        lifecyclePair: LifecyclePair = defaultLifecyclePair
): P {
    return attachPresenterDelegate(view, createPresenter,
            savedState, retain, manualHandleView, lifecyclePair
    ).presenter
}

@JvmOverloads
fun <V, P: MvpPresenter<V>> Activity.attachPresenterDelegate(
        view: V,
        createPresenter: () -> P,
        savedState: Bundle?,
        retain: Boolean = false,
        manualHandleView: Boolean = false,
        lifecyclePair: LifecyclePair = defaultLifecyclePair
): PresenterDelegate<V, P> {

    val retainedId: String = javaClass.name + hashCode()
    val delegate = PresenterDelegateImpl(createPresenter, savedState, retain, retainedId)

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
    application.registerActivityLifecycleCallbacks(lifecycleCallbacks.filter(this))
    return  PresenterDelegateChecker(delegate, manualHandleView)
}

@JvmOverloads
fun <V, P: MvpPresenter<V>> Fragment.attachPresenter(
        view: V,
        createPresenter: () -> P,
        savedState: Bundle?,
        retain: Boolean = false,
        manualHandleView: Boolean = false,
        lifecyclePair: LifecyclePair = defaultLifecyclePair
): P {
    return attachPresenterDelegate(view, createPresenter,
            savedState, retain, manualHandleView, lifecyclePair
    ).presenter
}

@JvmOverloads
fun <V, P: MvpPresenter<V>> Fragment.attachPresenterDelegate(
        view: V,
        createPresenter: () -> P,
        savedState: Bundle?,
        retain: Boolean = false,
        manualHandleView: Boolean = false,
        lifecyclePair: LifecyclePair = defaultLifecyclePair
): PresenterDelegate<V, P> {
    getPresenterDelegate<V, P>(view)?.let {
        return it
    }

    val retainedId = javaClass.name + hashCode()
    val delegate = PresenterDelegateImpl(createPresenter, savedState, retain, retainedId)

    val callbacks = object : FragmentLifecycleCallbacks() {

        override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
            delegate.onSaveState(outState)
        }
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View?, savedInstanceState: Bundle?) {
            if(!manualHandleView && lifecyclePair === CREATE_DESTROY_VIEW) {
                delegate.attachView(view)
            }
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
    attachPresenterDelegate(delegate, view, callbacks.filter(this))

    return PresenterDelegateChecker(delegate, manualHandleView)
}

internal class PresenterDelegateChecker<V, out P: MvpPresenter<V>>(
        val delegate: PresenterDelegateImpl<V, P>,
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

private const val TAG_PRESENTER_INFO_LIST = BuildConfig.APPLICATION_ID + "presenter_info_fragment"

private val Fragment.presenterInfoList: MutableList<PresenterDelegateInfo<*>>
    get() {
        val fm = childFragmentManager
        var fragment = fm.findFragmentByTag(TAG_PRESENTER_INFO_LIST) as? PresenterInfoListFragment?
        if(fragment == null) {
            fragment = PresenterInfoListFragment()
            fm.beginTransaction().add(fragment, TAG_PRESENTER_INFO_LIST).commitNow()
        }
        return fragment.list
    }

fun Fragment.detachPresenterByView(view: Any) {
    detachPresenterDelegate { it.view === view }
}

fun Fragment.detachPresenter(presenter: MvpPresenter<*>) {
    detachPresenterDelegate { it.delegate.presenter === presenter }
}

fun Fragment.detachPresenterDelegate(delegate: PresenterDelegate<*, *>) {
    detachPresenterDelegate { it.delegate === delegate }
}

internal class PresenterInfoListFragment : Fragment() {

    val list: MutableList<PresenterDelegateInfo<*>> = mutableListOf()
}

internal fun <V, P: MvpPresenter<V>> Fragment.attachPresenterDelegate(
        delegate: PresenterDelegate<V, P>, view: V, callbacks: FragmentLifecycleCallbacks) {
    with(presenterInfoList) {
        find { it.view === view }?.let {
            throw RuntimeException("view: $view already attached to $this")
        }
        add(PresenterDelegateInfo(delegate, view, callbacks))
    }
    fragmentManager.registerFragmentLifecycleCallbacks(callbacks, false)
}

internal fun Fragment.detachPresenterDelegate(predicate: (PresenterDelegateInfo<*>) -> Boolean) {
    with(presenterInfoList) {
        find(predicate)?.let {
            remove(it)
            fragmentManager.unregisterFragmentLifecycleCallbacks(it.callbacks)
        }
    }
}

@Suppress("UNCHECKED_CAST")
internal fun <V, P: MvpPresenter<V>> Fragment.getPresenterDelegate(view: V): PresenterDelegate<V, P>? {
    return presenterInfoList.find { it.view === view }
            ?.delegate as? PresenterDelegate<V, P>?
}

internal class PresenterDelegateInfo<V>(
        val delegate: PresenterDelegate<V, MvpPresenter<V>>,
        val view: V,
        val callbacks: FragmentLifecycleCallbacks
)

