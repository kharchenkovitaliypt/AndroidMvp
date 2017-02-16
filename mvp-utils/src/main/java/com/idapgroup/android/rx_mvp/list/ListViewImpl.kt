//package com.idap.worldbank.screen.country.list
//
//import android.annotation.SuppressLint
//import android.os.Bundle
//import android.support.v7.app.AlertDialog
//import android.support.v7.app.AppCompatActivity
//import android.support.v7.widget.LinearLayoutManager
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import com.idap.worldbank.App
//import com.idap.worldbank.R
//import com.idap.worldbank.data_model.data.Country
//import com.idap.worldbank.screen.country.details.CountryDetailsFragment
//import com.idapgroup.android.rx_mvp.list.ListMvp.View.LoadMoreState
//import com.idapgroup.android.rx_mvp.list.ListMvp.View.LoadMoreState.*
//import com.idapgroup.android.mvp.impl.LcePresenterFragment
//import com.idapgroup.android.rx_mvp.list.ListMvp
//import kotlinx.android.synthetic.main.fragment_country_list.*
//import kotlinx.android.synthetic.main.fragment_country_list.view.*
//import kotlinx.android.synthetic.main.item_action.view.*
//import javax.inject.Inject
//import javax.inject.Provider
//
//abstract class ListViewImpl<T> : ListMvp.View<T> {
//
//    abstract val presenter: ListMvp.Presenter
//
//    val onStart = {
//        Log.d("ScrollHandler", "onStart()")
//        presenter.onLoadUpMore()
//    }
//    val onEnd = {
//        Log.d("ScrollHandler", "onEnd()")
//        presenter.onLoadDownMore()
//    }
//    lateinit var scrollHandler: ScrollHandler
//
//    /** Down more */
//    override fun setDownLoadMoreState(state: LoadMoreState) {
//        when(state) {
//            HIDE -> {
//                scrollHandler.onEnd = null
//                adapter.downLoad.show(false)
//            }
//            LOAD -> {
//                scrollHandler.onEnd = null
//                adapter.downLoad.show(true)
//            }
//            MORE -> {
//                scrollHandler.onEnd = onEnd
//                adapter.downLoad.show(false)
//            }
//        }
//    }
//
//    override fun addDownItemList(itemList: List<Country>) {
//        adapter.addDownItemList(itemList)
//    }
//
//    override fun removeDownItemList(size: Int) {
//        adapter.removeDownItemList(size)
//    }
//
//    /** Up more */
//    override fun setUpLoadMoreState(state: LoadMoreState) {
//        when(state) {
//            HIDE -> {
//                scrollHandler.onStart = null
//                adapter.upLoad.show(false)
//            }
//            LOAD -> {
//                scrollHandler.onStart = null
//                adapter.upLoad.show(true)
//            }
//            MORE -> {
//                scrollHandler.onStart = onStart
//                adapter.upLoad.show(false)
//            }
//        }
//    }
//
//    override fun addUpItemList(itemList: List<Country>) {
//        adapter.addUpItemList(itemList)
//    }
//
//    override fun removeUpItemList(size: Int) {
//        adapter.removeUpItemList(size)
//    }
//}