package com.idapgroup.android.mvp.impl

import android.os.Bundle
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.app.AppCompatActivity
import com.idapgroup.android.mvp.impl.v2.LifecyclePair.*
import com.idapgroup.android.mvp.impl.v2.MVP_STRICT_MODE
import com.idapgroup.android.mvp.impl.v2.attachPresenter
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.properties.Delegates

class TestLifecycleView

class TestLifecyclePresenter : BasePresenter<TestLifecycleView>() {

    init {
        MVP_STRICT_MODE = true
    }

    val isAttachedView get() = view != null && onAttachedView && !onDetachedView

    var restored: Boolean by Delegates.notNull()

    var onCreated = false
    var onAttachedView = false
    var onDetachedView = false
    var onSavedState = false
    var onRestoredState = false

    override fun onCreate() {
        super.onCreate()
        onCreated = true
    }

    override fun onAttachedView(view: TestLifecycleView) {
        super.onAttachedView(view)
        if(restored) {
            assertFalse(onCreated)
            assertTrue(onRestoredState)
        } else {
            assertTrue(onCreated)
            assertFalse(onRestoredState)
        }
        onAttachedView = true
    }

    override fun onDetachedView() {
        super.onDetachedView()
        onDetachedView = true
    }

    override fun onSaveState(savedState: Bundle) {
        super.onSaveState(savedState)
        savedState.putString("testKey", "testValue")
        onSavedState = true
    }

    override fun onRestoreState(savedState: Bundle) {
        super.onRestoreState(savedState)
        val savedValue = savedState.getString("testKey")
        assertThat(savedValue, IsEqual("testValue"))
        onRestoredState = true
    }
}

class ActivityResumePausePresenterViewLifecycleTest {

    @get:Rule
    val activityRule = ActivityTestRule(Activity::class.java)

    @Test fun testBase() {
        waitForIdleSyncAfter {
            activityRule.activity.recreate()
        }
        val curActivity = currentActivity as Activity
        waitForIdleSyncAfter {
            curActivity.destroying = true
        }
    }

    class Activity : AppCompatActivity() {

        val view = TestLifecycleView()
        lateinit var presenter: TestLifecyclePresenter

        var destroying = false

        override fun onCreate(savedState: Bundle?) {
            super.onCreate(savedState)
            val createPresenter = { TestLifecyclePresenter().apply {
                restored = savedState != null
            } }
            presenter = attachPresenter(
                    this, view, createPresenter, savedState, false, lifecyclePair = RESUME_PAUSE)
            assertFalse(presenter.isAttachedView)
        }

        override fun onResume() {
            assertFalse(presenter.isAttachedView)
            super.onResume()
            assertTrue(presenter.isAttachedView)
        }

        override fun onPause() {
            assertTrue(presenter.isAttachedView)
            super.onPause()
            assertFalse(presenter.isAttachedView)
            assertFalse(presenter.onSavedState)
        }

        override fun onStop() {
            super.onStop()
            if(!destroying) {
                assertTrue(presenter.onSavedState)
            }
        }
    }
}

@RunWith(AndroidJUnit4::class)
class ActivityStartStopPresenterViewLifecycleTest {

    @get:Rule
    val activityRule = ActivityTestRule(Activity::class.java)

    @Test fun testBase() {
        waitForIdleSyncAfter {
            activityRule.activity.recreate()
        }
    }

    class Activity : AppCompatActivity() {

        val view = TestLifecycleView()
        lateinit var presenter: TestLifecyclePresenter

        override fun onCreate(savedState: Bundle?) {
            super.onCreate(savedState)
            val createPresenter = { TestLifecyclePresenter().apply {
                restored = savedState != null
            } }
            presenter = attachPresenter(
                    this, view, createPresenter, savedState, false, lifecyclePair = START_STOP)
            assertFalse(presenter.isAttachedView)
        }

        override fun onStart() {
            assertFalse(presenter.isAttachedView)
            super.onStart()
            assertTrue(presenter.isAttachedView)
        }

        override fun onStop() {
            assertTrue(presenter.isAttachedView)
            super.onStop()
            assertFalse(presenter.isAttachedView)
        }
    }
}

@RunWith(AndroidJUnit4::class)
class ActivityCreateDestroyPresenterViewLifecycleTest {

    @get:Rule
    val activityRule = ActivityTestRule(Activity::class.java)

    @Test fun testBase() {
        waitForIdleSyncAfter {
            activityRule.activity.recreate()
        }
    }

    class Activity : AppCompatActivity() {

        val view = TestLifecycleView()
        lateinit var presenter: TestLifecyclePresenter

        override fun onCreate(savedState: Bundle?) {
            super.onCreate(savedState)
            val createPresenter = { TestLifecyclePresenter().apply {
                restored = savedState != null
            } }
            presenter = attachPresenter(
                    this, view, createPresenter, savedState, false, lifecyclePair = CREATE_DESTROY_VIEW)
            assertTrue(presenter.isAttachedView)
        }

        override fun onDestroy() {
            assertTrue(presenter.isAttachedView)
            super.onDestroy()
            assertFalse(presenter.isAttachedView)
        }
    }
}

