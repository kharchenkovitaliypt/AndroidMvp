package com.idapgroup.android.mvp.impl

import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.v7.app.AppCompatActivity

fun waitForIdleSyncAfter(action: () -> Unit) {
    with(getInstrumentation()) {
        runOnMainSync { action() }
        waitForIdleSync()
    }
}

class MockActivity : AppCompatActivity()

class MockPresenter : BasePresenter<MockMvpView>()

interface MockMvpView