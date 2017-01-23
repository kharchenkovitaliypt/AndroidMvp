package com.idapgroup.android.mvp.impl

import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentTransaction


fun FragmentActivity.addFragment(fragment: Fragment, tag: String? = null, @IdRes containerId: Int = android.R.id.content) {
    commitTransaction { it.add(containerId, fragment, tag) }
}

fun FragmentActivity.replaceFragment(fragment: Fragment,
                                     tag: String? = null, @IdRes containerId: Int = android.R.id.content,
                                     addToBackStack: Boolean = false) {
    commitTransaction { transact ->
        transact.replace(containerId, fragment, tag)
                .apply {
                    if(addToBackStack) {
                        addToBackStack(null)
                    }
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