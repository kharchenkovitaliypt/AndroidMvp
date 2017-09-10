package com.idapgroup.android.mvp.impl

import android.os.Bundle
import android.support.test.rule.ActivityTestRule
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.idapgroup.android.mvp.impl.v2.Retain
import com.idapgroup.android.mvp.impl.v2.attachPresenter
import com.idapgroup.android.mvp.impl.v2.retainedPresenters
import org.junit.Rule
import org.junit.Test

class PresenterFragmentTest {

    @get:Rule
    val activityRule = ActivityTestRule(TestActivity::class.java)
    val activity get() = activityRule.activity!!

    val presenter1 = TestPresenter()
    val fragment1 = TestFragment("fragment_1", presenter1)

    val presenter2 = TestPresenter()
    val fragment2 = TestFragment("fragment_2", presenter2)

    val presenter3 = TestPresenter()
    val fragment3 = TestFragment("fragment_3", presenter3)
    
    val allFragments = arrayOf(fragment1, fragment2, fragment3)

    @Test fun testRestoreAddedFragmentPresenter() {
        waitForIdleSyncAfter {
            allFragments.forEach {
                activity.addFragment(it, it.key!!)
            }
        }
        checkRetainedPresenters(*allFragments)
    }

    @Test fun testRestoreHiddenFragmentPresenter() {
        waitForIdleSyncAfter {
            activity.addFragment(fragment1, fragment1.key!!)
            activity.hideFragment(fragment1)
            activity.addFragment(fragment2, fragment2.key!!)
            activity.hideFragment(fragment2)
        }
        checkRetainedPresenters(fragment1, fragment2)
    }

    @Test fun testRestoreReplacedFragmentPresenter() {
        waitForIdleSyncAfter {
            activity.addFragment(fragment1, fragment1.key!!)
            activity.replaceFragment(fragment2, fragment2.key!!, addToBackStack = true)
            activity.replaceFragment(fragment3, fragment3.key!!, addToBackStack = true)
        }
        checkRetainedPresenters(fragment1, afterRecreate = { activity ->
            assert(retainedPresenters.size == 2)
            activity.popBackStack()
            assert(retainedPresenters.size == 1)
            activity.popBackStack()
        })
    }

    fun checkRetainedPresenters(vararg fragments: TestFragment,
                                afterRecreate: (AppCompatActivity) -> Unit = {}) {
        waitForIdleSyncAfter {
            activity.recreate()
        }
        val curActivity = currentActivity
        waitForIdleSyncAfter {
            afterRecreate(curActivity)
        }
        waitForIdleSyncAfter {
            assert(retainedPresenters.isEmpty())
        }
        checkPresentersEquality(*fragments)
    }

    fun checkPresentersEquality(vararg fragments: TestFragment) {
        val curActivity = currentActivity
        waitForIdleSyncAfter {
            fragments.forEach {
                val recreatedFragment = curActivity.getFragment(it.key!!)
                val recreatedPresenter = (recreatedFragment as TestFragment).presenter
                assert(recreatedFragment !== it)
                assert(recreatedPresenter == it.presenter!!)
            }
        }
    }

    class TestFragment constructor(
            val key: String? = null,
            val presenter: TestPresenter? = null
    ) : Fragment(), TestMvpView {

        private lateinit var p: TestPresenter

        override fun onCreate(savedState: Bundle?) {
            super.onCreate(savedState)
            p = attachPresenter(this, this, { presenter!! }, Retain(savedState))
        }
    }
}


