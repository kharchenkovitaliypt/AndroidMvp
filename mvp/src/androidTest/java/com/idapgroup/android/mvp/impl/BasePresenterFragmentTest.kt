//package com.idapgroup.android.mvp.impl
//
//import android.support.test.InstrumentationRegistry.getInstrumentation
//import android.support.test.rule.ActivityTestRule
//import android.support.v7.app.AppCompatActivity
//import com.idapgroup.android.mvp.BasePresenter
//import com.idapgroup.android.mvp.BasePresenterFragment
//import com.idapgroup.android.mvp.tmpPresenterDelegatesStorage
//import junit.framework.Assert.assertNotSame
//import org.hamcrest.core.Is
//import org.junit.Assert.assertThat
//import org.junit.Rule
//import org.junit.Test
//
//class BasePresenterFragmentTest {
//
//    @get:Rule val activityRule = ActivityTestRule(MockActivity::class.java)
//    val activity: AppCompatActivity
//            get() = activityRule.activity
//
//    @Test fun testRestoreAddedFragmentRetainedPresenters() {
//        // Because tmpPresenterDelegatesStorage is static
//        synchronized(tmpPresenterDelegatesStorage) {
//
//            val originalPresenter1 = MockMvpPresenter()
//            val originalPresenter2 = MockMvpPresenter()
//            val originalPresenter3 = MockMvpPresenter()
//
//            activity.runOnUiThread {
//                activity.addFragment(MockBasePresenterFragment(originalPresenter1), "fragment_1")
//                activity.addFragment(MockBasePresenterFragment(originalPresenter2), "fragment_2")
//                activity.addFragment(MockBasePresenterFragment(originalPresenter3), "fragment_3")
//
//            }
//            getInstrumentation().waitForIdleSync()
//
//            activity.runOnUiThread {
//                activity.recreate()
//                assert(tmpPresenterDelegatesStorage.isEmpty())
//
//                checkMockPresenter(activity, "fragment_1", originalPresenter1)
//                checkMockPresenter(activity, "fragment_2", originalPresenter2)
//                checkMockPresenter(activity, "fragment_3", originalPresenter3)
//            }
//            getInstrumentation().waitForIdleSync()
//        }
//    }
//
//    @Test fun testRestoreHiddenFragmentRetainedPresenter() {
//        // Because tmpPresenterDelegatesStorage is static
//        synchronized(tmpPresenterDelegatesStorage) {
//
//            val originalPresenter1 = MockMvpPresenter()
//            val originalFragment1 = MockBasePresenterFragment(originalPresenter1)
//
//            val originalPresenter2 = MockMvpPresenter()
//            val originalFragment2 = MockBasePresenterFragment(originalPresenter2)
//
//            activity.runOnUiThread {
//                activity.addFragment(originalFragment1, "fragment_1")
//                activity.hideFragment(originalFragment1)
//                activity.addFragment(originalFragment2, "fragment_2")
//                activity.hideFragment(originalFragment2)
//            }
//            getInstrumentation().waitForIdleSync()
//
//            activity.runOnUiThread {
//                activity.recreate()
//                assert(tmpPresenterDelegatesStorage.isEmpty())
//
//                checkMockPresenter(activity, "fragment_1", originalPresenter1)
//                checkMockPresenter(activity, "fragment_2", originalPresenter2)
//            }
//            getInstrumentation().waitForIdleSync()
//        }
//    }
//
//    @Test fun testRestoreReplacedFragmentRetainedPresenters() {
//        // Because tmpPresenterDelegatesStorage is static
//        synchronized(tmpPresenterDelegatesStorage) {
//            val originalPresenter1 = MockMvpPresenter()
//            val originalPresenter2 = MockMvpPresenter()
//            val originalPresenter3 = MockMvpPresenter()
//
//            activity.runOnUiThread {
//                activity.addFragment(fragment = MockBasePresenterFragment(originalPresenter1), tag = "fragment_1")
//                activity.replaceFragment(fragment = MockBasePresenterFragment(originalPresenter2),
//                        tag = "fragment_2", addToBackStack = true)
//                activity.replaceFragment(fragment = MockBasePresenterFragment(originalPresenter3),
//                        tag = "fragment_3", addToBackStack = true)
//            }
//            getInstrumentation().waitForIdleSync()
//
//            activity.runOnUiThread {
//                activity.recreate()
//                assert(tmpPresenterDelegatesStorage.isEmpty())
//
//                checkMockPresenter(activity, "fragment_1", originalPresenter1)
//                checkMockPresenter(activity, "fragment_2", originalPresenter2)
//                checkMockPresenter(activity, "fragment_3", originalPresenter3)
//            }
//            getInstrumentation().waitForIdleSync()
//        }
//    }
//
//    fun checkMockPresenter(activity: AppCompatActivity,
//                           recreatedFragmentTag: String, originPresenter: MockMvpPresenter) {
//        val recreatedFragment = activity.getFragment(recreatedFragmentTag);
//        assertNotSame(recreatedFragment, originPresenter)
//        assertThat((recreatedFragment as MockBasePresenterFragment).presenter, Is.`is`(originPresenter))
//    }
//
//    class MockBasePresenterFragment constructor(val mockPresenter: MockMvpPresenter?)
//        : BasePresenterFragment<MockMvpView, MockMvpPresenter>() {
//
//        constructor() : this(null)
//
//        /** if fragment restored must be never be called */
//        override fun onCreatePresenter() = mockPresenter as MockMvpPresenter
//    }
//
//    class MockMvpPresenter : BasePresenter<MockMvpView>()
//
//    class MockMvpView
//}
//
//class MockActivity : AppCompatActivity()