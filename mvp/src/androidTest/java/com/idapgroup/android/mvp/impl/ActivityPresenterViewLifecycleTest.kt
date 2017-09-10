package com.idapgroup.android.mvp.impl

import android.os.Bundle
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.app.AppCompatActivity
import com.idapgroup.android.mvp.impl.v2.LifecyclePair.*
import com.idapgroup.android.mvp.impl.v2.attachPresenter
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

class ActivityResumePausePresenterViewLifecycleTest {

    @get:Rule
    val activityRule = ActivityTestRule(Activity::class.java)

    @Test fun testBase() { }

    class Activity : AppCompatActivity() {

        class TestView

        class TestPresenter : BasePresenter<TestView>() {
            val isViewAttached get() = view != null
        }

        val view = TestView()
        lateinit var presenter: TestPresenter

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            presenter = attachPresenter(
                    this, view, ::TestPresenter, lifecyclePair = RESUME_PAUSE)
            assertFalse(presenter.isViewAttached)
        }

        override fun onResume() {
            assertFalse(presenter.isViewAttached)
            super.onResume()
            assertTrue(presenter.isViewAttached)
        }

        override fun onPause() {
            assertTrue(presenter.isViewAttached)
            super.onPause()
            assertFalse(presenter.isViewAttached)
        }
    }
}

@RunWith(AndroidJUnit4::class)
class ActivityStartStopPresenterViewLifecycleTest {

    class TestView

    class TestPresenter : BasePresenter<TestView>() {
        val isViewAttached get() = view != null
    }

    @get:Rule
    val activityRule = ActivityTestRule(Activity::class.java)

    @Test fun testBase() {}

    class Activity : AppCompatActivity() {

        val view = TestView()
        lateinit var presenter: TestPresenter

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            presenter = attachPresenter(
                    this, view, ::TestPresenter, lifecyclePair = START_STOP)
            assertFalse(presenter.isViewAttached)
        }

        override fun onStart() {
            assertFalse(presenter.isViewAttached)
            super.onStart()
            assertTrue(presenter.isViewAttached)
        }

        override fun onStop() {
            assertTrue(presenter.isViewAttached)
            super.onStop()
            assertFalse(presenter.isViewAttached)
        }
    }
}

@RunWith(AndroidJUnit4::class)
class ActivityCreateDestroyPresenterViewLifecycleTest {

    class TestView

    class TestPresenter : BasePresenter<TestView>() {
        val isViewAttached get() = view != null
    }

    @Test fun testBase() {}

    @get:Rule
    val activityRule = ActivityTestRule(Activity::class.java)

    class Activity : AppCompatActivity() {

        val view = TestView()
        lateinit var presenter: TestPresenter

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            presenter = attachPresenter(
                    this, view, ::TestPresenter, lifecyclePair = CREATE_DESTROY_VIEW)
            assertTrue(presenter.isViewAttached)
        }

        override fun onDestroy() {
            assertTrue(presenter.isViewAttached)
            super.onDestroy()
            assertFalse(presenter.isViewAttached)
        }
    }
}

