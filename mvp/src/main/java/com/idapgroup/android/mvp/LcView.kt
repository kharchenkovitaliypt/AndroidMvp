package com.idapgroup.android.mvp

/** Represents view with two states(load, content)  */
interface LcView {

    /** Show load view indicating process  */
    fun showLoad()

    /** Show content view  */
    fun showContent()
}