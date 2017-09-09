package com.idapgroup.android.mvp

interface ProgressView {
    fun showProgress(progress: Float = 0F)
    fun hideProgress()
}