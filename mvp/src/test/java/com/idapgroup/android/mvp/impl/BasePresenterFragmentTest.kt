package com.idapgroup.android.mvp.impl

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.idapgroup.android.mvp.BasePresenter
import com.idapgroup.android.mvp.BasePresenterFragment
import com.idapgroup.android.mvp.tmpPresenterDelegatesStorage
import junit.framework.Assert.assertNotSame
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class BasePresenterFragmentTest {

    lateinit var activity: FragmentActivity

    @Before fun setUp() {
        activity = Robolectric.buildActivity(FragmentActivity::class.java)
                .create().start().resume().get()
    }

    @Test fun testRestoreVisibleFragmentRetainedPresenter() {
        // Because tmpPresenterDelegatesStorage is static
        synchronized(tmpPresenterDelegatesStorage) {
            val originalPresenter = MockMvpPresenter()
            val originalFragment = MockBasePresenterFragment(originalPresenter)

            addFragment(originalFragment, "tag")

            activity.recreate()
            assert(tmpPresenterDelegatesStorage.isEmpty())

            val recreatedFragment = getFragment("tag")
            assertNotSame(originalFragment, recreatedFragment)
            assertThat((recreatedFragment as MockBasePresenterFragment).presenter, `is`(originalPresenter))
        }
    }

    @Test fun testRestoreHiddenFragmentRetainedPresenter() {
        // Because tmpPresenterDelegatesStorage is static
        synchronized(tmpPresenterDelegatesStorage) {
            val originalPresenter = MockMvpPresenter()
            val originalFragment = MockBasePresenterFragment(originalPresenter)

            addFragment(originalFragment, "tag")
            hideFragment(originalFragment)

            activity.recreate()
            assert(tmpPresenterDelegatesStorage.isEmpty())

            val recreatedFragment = getFragment("tag")
            assertNotSame(originalFragment, recreatedFragment)
            assertThat((recreatedFragment as MockBasePresenterFragment).presenter, `is`(originalPresenter))
        }
    }

//    @Test fun testRestoreManyFragmentRetainedPresenters() {
//        // Because tmpPresenterDelegatesStorage is static
//        synchronized(tmpPresenterDelegatesStorage) {
//            // Fragment 1
//            val originalPresenter1 = MockMvpPresenter()
//            val originalFragment1 = MockBasePresenterFragment(originalPresenter1)
//            addFragment(originalFragment1, "fragment_1")
//            // Fragment 2
//            val originalPresenter2 = MockMvpPresenter()
//            val originalFragment2 = MockBasePresenterFragment(originalPresenter2)
//            addFragment(originalFragment2, "fragment_2")
//            // Fragment 3
//            val originalPresenter3 = MockMvpPresenter()
//            val originalFragment3 = MockBasePresenterFragment(originalPresenter3)
//            addFragment(originalFragment2, "fragment_3")
//
//            activity.recreate()
//            assert(tmpPresenterDelegatesStorage.isEmpty())
//
//            val recreatedFragment = getFragment(tagFragment1)
//            assertNotSame(originalFragment, recreatedFragment)
//            assertThat((recreatedFragment as MockBasePresenterFragment).presenter, `is`(originalPresenter))
//        }
//    }

    class MockBasePresenterFragment constructor(val mockPresenter: MockMvpPresenter?)
        : BasePresenterFragment<MockMvpView, MockMvpPresenter>() {

        constructor(): this(null)

        /** if fragment restored must be never be called */
        override fun createPresenter() = mockPresenter as MockMvpPresenter
    }

    class MockMvpPresenter : BasePresenter<MockMvpView>()

    class MockMvpView

    fun addFragment(fragment: Fragment, tag: String) {
        activity.supportFragmentManager.beginTransaction()
                .add(fragment, tag)
                .commitNow()
    }

    fun hideFragment(fragment: Fragment) {
        activity.supportFragmentManager.beginTransaction()
                .hide(fragment)
                .commitNow()
    }

    fun removeFragment(fragment: Fragment) {
        activity.supportFragmentManager.beginTransaction()
                .hide(fragment)
                .commitNow()
    }

    fun getFragment(tag: String) = activity.supportFragmentManager.findFragmentByTag(tag)!!
}
