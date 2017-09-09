package com.idapgroup.android.mvp

/** Represents view with three states(load, content, raw error with retry)  */
interface RawLceView : LcView {

    /**
     * Show an error message with retry ability
     * @param error Raw error
     * @param retry Retry action
     */
    fun showError(error: Throwable, retry: (() -> Unit)? = null)
}