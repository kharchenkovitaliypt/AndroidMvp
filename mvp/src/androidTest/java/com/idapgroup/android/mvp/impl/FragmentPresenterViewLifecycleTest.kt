package com.idapgroup.android.mvp.impl

import android.os.Bundle
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v4.app.Fragment
import com.idapgroup.android.mvp.impl.v2.LifecyclePair.*
import com.idapgroup.android.mvp.impl.v2.attachPresenter
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FragmentPresenterViewLifecycleTest {

    class TestView

    class TestPresenter : BasePresenter<TestView>() {
        val isViewAttached get() = view != null
    }

    @get:Rule
    val activityRule = ActivityTestRule(TestActivity::class.java)

    @Test fun testResumePause() {
        testLifecycle(ResumePauseFragment())
    }

    @Test fun testStartStop() {
        testLifecycle(StartStopFragment())
    }

    @Test fun testCreateDestroy() {
        testLifecycle(CreateDestroyFragment())
    }

    fun testLifecycle(fragment: Fragment) {
        waitForIdleSyncAfter {
            activityRule.activity.addFragment(fragment)
        }
        waitForIdleSyncAfter {
            activityRule.activity.removeFragment(fragment)
        }
    }

    class ResumePauseFragment : Fragment() {

        val view = TestView()
        lateinit var presenter: TestPresenter

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            presenter = attachPresenter(
                    this, view, ::TestPresenter, lifecyclePair = RESUME_PAUSE)
            assertFalse(presenter.isViewAttached)
        }

        override fun onResume() {
            super.onResume()
            assertFalse(presenter.isViewAttached)
        }
        // attachView()

        override fun onPause() {
            super.onPause()
            assertTrue(presenter.isViewAttached)
        }
        // detachView()

        override fun onStop() {
            super.onStop()
            assertFalse(presenter.isViewAttached)
        }
    }

    class StartStopFragment : Fragment() {

        val view = TestView()
        lateinit var presenter: TestPresenter

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            presenter = attachPresenter(
                    this, view, ::TestPresenter, lifecyclePair = START_STOP)
        }

        override fun onStart() {
            super.onStart()
            assertFalse(presenter.isViewAttached)
        }
        // attachView()

        override fun onResume() {
            assertTrue(presenter.isViewAttached)
            super.onResume()
        }

        override fun onStop() {
            super.onStop()
            assertTrue(presenter.isViewAttached)
        }
        // detachView()

        override fun onDestroyView() {
            assertFalse(presenter.isViewAttached)
            super.onDestroyView()
        }
    }

    class CreateDestroyFragment : Fragment() {

        val view = TestView()
        lateinit var presenter: TestPresenter

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            presenter = attachPresenter(
                    this, view, ::TestPresenter, lifecyclePair = CREATE_DESTROY_VIEW)
            // attachView()
            assertTrue(presenter.isViewAttached)
        }

        override fun onDestroyView() {
            super.onDestroyView()
            assertTrue(presenter.isViewAttached)
        }
        // detachView()

        override fun onDestroy() {
            assertFalse(presenter.isViewAttached)
            super.onDestroy()
        }
    }
}