package com.idapgroup.android.mvp.impl

import android.os.Bundle
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.idapgroup.android.mvp.impl.v2.LifecyclePair
import com.idapgroup.android.mvp.impl.v2.attachPresenter
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FragmentPresenterViewLifecycleTest {

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
            activityRule.activity.recreate()
        }
    }

    class ResumePauseFragment : Fragment() {

        val mvpView = TestLifecycleView()
        lateinit var presenter: TestLifecyclePresenter

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return View(context)
        }

        override fun onViewCreated(view: View?, savedState: Bundle?) {
            super.onViewCreated(view, savedState)
            val createPresenter = { TestLifecyclePresenter().apply {
                restored = savedState != null
            } }
            presenter = attachPresenter(
                    mvpView, createPresenter, savedState, false, lifecyclePair = LifecyclePair.RESUME_PAUSE)
            assertFalse(presenter.isAttachedView)
        }

        override fun onResume() {
            super.onResume()
            assertFalse(presenter.isAttachedView)
        }
        // attachView()

        override fun onPause() {
            super.onPause()
            assertTrue(presenter.isAttachedView)
        }
        // detachView()

        override fun onStop() {
            super.onStop()
            assertFalse(presenter.isAttachedView)
        }
    }

    class StartStopFragment : Fragment() {

        val mvpView = TestLifecycleView()
        lateinit var presenter: TestLifecyclePresenter

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return View(context)
        }

        override fun onViewCreated(view: View?, savedState: Bundle?) {
            super.onViewCreated(view, savedState)
            val createPresenter = { TestLifecyclePresenter().apply {
                restored = savedState != null
            } }
            presenter = attachPresenter(
                    mvpView, createPresenter, savedState, false, lifecyclePair = LifecyclePair.START_STOP)
        }

        override fun onStart() {
            super.onStart()
            assertFalse(presenter.isAttachedView)
        }
        // attachView()

        override fun onResume() {
            assertTrue(presenter.isAttachedView)
            super.onResume()
        }

        override fun onStop() {
            super.onStop()
            assertTrue(presenter.isAttachedView)
        }
        // detachView()

        override fun onDestroyView() {
            assertFalse(presenter.isAttachedView)
            super.onDestroyView()
        }
    }

    class CreateDestroyFragment : Fragment() {

        val mvpView = TestLifecycleView()
        lateinit var presenter: TestLifecyclePresenter

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return View(context)
        }

        override fun onViewCreated(view: View?, savedState: Bundle?) {
            super.onViewCreated(view, savedState)
            val createPresenter = { TestLifecyclePresenter().apply {
                restored = savedState != null
            } }
            presenter = attachPresenter(
                    mvpView, createPresenter, savedState, false, lifecyclePair = LifecyclePair.CREATE_DESTROY_VIEW)
        }

        override fun onStart() {
            super.onStart()
            assertTrue(presenter.isAttachedView)
        }

        override fun onDestroyView() {
            super.onDestroyView()
            assertTrue(presenter.isAttachedView)
        }
        // detachView()

        override fun onDestroy() {
            assertFalse(presenter.isAttachedView)
            super.onDestroy()
        }
    }
}