package com.idapgroup.android.mvp.impl.v2

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentManager.FragmentLifecycleCallbacks
import android.view.View

fun ActivityLifecycleCallbacks.filter(activity: Activity): ActivityLifecycleCallbacks {
    return filter { it === activity }
}

fun ActivityLifecycleCallbacks.filter(predicate: (Activity) -> Boolean): ActivityLifecycleCallbacks {
    val delegate = this
    return object : ActivityLifecycleCallbacks {

        override fun onActivityCreated(a: Activity, savedInstanceState: Bundle?) {
            if(predicate(a)) delegate.onActivityCreated(a, savedInstanceState)
        }
        override fun onActivitySaveInstanceState(a: Activity, outState: Bundle) {
            if(predicate(a)) delegate.onActivitySaveInstanceState(a, outState)
        }
        override fun onActivityStarted(a: Activity) {
            if(predicate(a)) delegate.onActivityStarted(a)
        }
        override fun onActivityResumed(a: Activity) {
            if(predicate(a)) delegate.onActivityResumed(a)
        }
        override fun onActivityPaused(a: Activity) {
            if(predicate(a)) delegate.onActivityPaused(a)
        }
        override fun onActivityStopped(a: Activity) {
            if(predicate(a)) delegate.onActivityStopped(a)
        }
        override fun onActivityDestroyed(a: Activity) {
            if(predicate(a)) delegate.onActivityDestroyed(a)
        }
    }
}

fun FragmentLifecycleCallbacks.filter(fragment: Fragment): FragmentLifecycleCallbacks {
    return filter { it === fragment }
}

fun FragmentLifecycleCallbacks.filter(predicate: (Fragment) -> Boolean): FragmentLifecycleCallbacks {
    val delegate = this
    return object : FragmentLifecycleCallbacks() {

        override fun onFragmentPreAttached(fm: FragmentManager?, f: Fragment, context: Context?) {
            if(predicate(f)) delegate.onFragmentPreAttached(fm, f, context)
        }
        override fun onFragmentAttached(fm: FragmentManager?, f: Fragment, context: Context?) {
            if(predicate(f)) delegate.onFragmentAttached(fm, f, context)
        }
        override fun onFragmentActivityCreated(fm: FragmentManager?, f: Fragment, savedInstanceState: Bundle?) {
            if(predicate(f)) delegate.onFragmentActivityCreated(fm, f, savedInstanceState)
        }
        override fun onFragmentCreated(fm: FragmentManager?, f: Fragment, savedInstanceState: Bundle?) {
            if(predicate(f)) delegate.onFragmentCreated(fm, f, savedInstanceState)
        }
        override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
            if(predicate(f)) delegate.onFragmentSaveInstanceState(fm, f, outState)
        }
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View?, savedInstanceState: Bundle?) {
            if(predicate(f)) delegate.onFragmentViewCreated(fm, f, v, savedInstanceState)
        }
        override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
            if(predicate(f)) delegate.onFragmentStarted(fm, f)
        }
        override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
            if(predicate(f)) delegate.onFragmentResumed(fm, f)
        }
        override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
            if(predicate(f)) delegate.onFragmentPaused(fm, f)
        }
        override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
            if(predicate(f)) delegate.onFragmentStopped(fm, f)
        }
        override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
            if(predicate(f)) delegate.onFragmentViewDestroyed(fm, f)
        }
        override fun onFragmentDestroyed(fm: FragmentManager?, f: Fragment) {
            if(predicate(f)) delegate.onFragmentDestroyed(fm, f)
        }
        override fun onFragmentDetached(fm: FragmentManager?, f: Fragment) {
            if(predicate(f)) delegate.onFragmentDetached(fm, f)
        }
    }
}