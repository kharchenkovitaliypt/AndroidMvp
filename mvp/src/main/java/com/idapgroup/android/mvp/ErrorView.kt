package com.idapgroup.android.mvp

interface ErrorView {
    /**
     * Show an error message with retry ability
     * @param errorMessage A string representing an error.
     * @param retry handle retry request
     */
    fun showError(errorMessage: String, retry: (() -> Unit)?)
}
