package com.idapgroup.android.mvp.impl

import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.v7.app.AppCompatActivity
import com.idapgroup.android.mvp.impl.v2.MVP_STRICT_MODE

fun waitForIdleSyncAfter(action: () -> Unit) {
    with(getInstrumentation()) {
        runOnMainSync { action() }
        waitForIdleSync()
    }
}

class TestActivity : AppCompatActivity()

class TestPresenter : BasePresenter<TestMvpView>() {
    init {
        MVP_STRICT_MODE = true
    }
}

interface TestMvpView