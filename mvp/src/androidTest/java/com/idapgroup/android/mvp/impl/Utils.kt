package com.idapgroup.android.mvp.impl

import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.v7.app.AppCompatActivity
import com.idapgroup.android.mvp.impl.v2.mvpStrictMode

fun waitForIdleSyncAfter(action: () -> Unit) {
    with(getInstrumentation()) {
        runOnMainSync { action() }
        waitForIdleSync()
    }
}

class TestActivity : AppCompatActivity()

class TestPresenter : BasePresenter<TestMvpView>() {
    init {
        mvpStrictMode = true
    }
}

interface TestMvpView