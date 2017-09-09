package com.idapgroup.android.mvp.impl

import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import android.support.test.runner.lifecycle.Stage
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity


fun FragmentActivity.addFragment(fragment: Fragment, tag: String) {
    commitTransaction { it.add(android.R.id.content, fragment, tag) }
}

fun FragmentActivity.replaceFragment(fragment: Fragment, tag: String, addToBackStack: Boolean = false) {
    commitTransaction {
        it.replace(android.R.id.content, fragment, tag).apply {
            if(addToBackStack) addToBackStack(null)
        }
    }
}

fun FragmentActivity.hideFragment(fragment: Fragment) {
    commitTransaction { it.hide(fragment) }
}

fun FragmentActivity.removeFragment(fragment: Fragment) {
    commitTransaction { it.remove(fragment) }
}

fun FragmentActivity.commitTransaction(configTransact: (FragmentTransaction) -> FragmentTransaction) {
    configTransact(supportFragmentManager.beginTransaction()).commit()
    supportFragmentManager.executePendingTransactions()
}

fun FragmentActivity.getFragment(tag: String) = supportFragmentManager.findFragmentByTag(tag)!!

fun FragmentActivity.popBackStack() {
    supportFragmentManager.popBackStackImmediate()
}

val currentActivity: AppCompatActivity
    get() {
    with(getInstrumentation()) {
        waitForIdleSync()
        var activity: AppCompatActivity? = null
        runOnMainSync {
            activity = ActivityLifecycleMonitorRegistry.getInstance()
                    .getActivitiesInStage(Stage.RESUMED)
                    .first() as AppCompatActivity
        }
        return activity!!
    }
}