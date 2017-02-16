package com.idapgroup.android.rx_mvp.list

interface ListMvp {

    interface Presenter {
        fun onLoadUpMore()
        fun onLoadDownMore()
    }

    interface View<in T> {

        enum class LoadMoreState { HIDE, LOAD, MORE }

        fun setUpLoadMoreState(state: LoadMoreState)
        fun setDownLoadMoreState(state: LoadMoreState)

        fun addUpItemList(itemList: List<T>)
        fun removeUpItemList(size: Int)
        fun addDownItemList(itemList: List<T>)
        fun removeDownItemList(size: Int)
    }
}