package com.idapgroup.android.mvp

/** Represents view with three states(loading, contentView, error with retry)  */
interface LceView : LcView {
    /**
     * Show an error message with retry ability
     * @param errorMessage Message representing an error.
     * @param retry Retry action
     */
    fun showError(errorMessage: String, retry: (() -> Unit)? = null)
}