package com.idapgroup.android.mvp

/** Interface representing a View with three states(loading, contentView, error)  */
interface LceView : ErrorView {

    /** Show a view with a loadView bar indicating a loading process  */
    fun showLoad()

    /** Show a view with a contentView  */
    fun showContent()
}