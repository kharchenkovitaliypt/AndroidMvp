package com.idapgroup.android.rx_mvp.list

import android.os.Bundle
import com.idapgroup.android.rx_mvp.list.ListMvp.View.LoadMoreState.HIDE
import com.idapgroup.android.rx_mvp.list.ListMvp.View.LoadMoreState.MORE

abstract class ListPresenter<in T> : ListMvp.Presenter {

    open val pageSize = 5
    val maxItemsSize = 15

    var offset: Int? = null
    var size: Int? = null

    abstract val view: ListMvp.View<T>

    fun showStartItemList(itemList: List<T>) {
        offset = 0
        size = itemList.size

        view.setUpLoadMoreState(HIDE)
        view.addDownItemList(itemList)
        view.setDownLoadMoreState(if(itemList.size >= pageSize) MORE else HIDE)
    }

    fun addDownItemList(itemList: List<T>) {
        var size = this.size!!
        size += itemList.size

        view.setDownLoadMoreState(if(itemList.size >= pageSize) MORE else HIDE)
        view.addDownItemList(itemList)

        if(size > maxItemsSize) {
            val removeSize = size - maxItemsSize
            size -= removeSize
            this.offset = this.offset!! + removeSize
            view.removeUpItemList(removeSize)
            view.setUpLoadMoreState(MORE)
        }
        this.size = size
    }

    fun addUpItemList(itemList: List<T>, offset: Int) {
        var size = this.size!!
        size += itemList.size

        view.setUpLoadMoreState(if(offset > 0) MORE else HIDE)
        view.addUpItemList(itemList)

        if(size > maxItemsSize) {
            val removeSize = size - maxItemsSize
            size -= removeSize
            view.removeDownItemList(removeSize)
            view.setDownLoadMoreState(MORE)
        }
        this.offset = offset
        this.size = size
    }

    fun onSaveState(savedState: Bundle) {
        savedState.putInt("offset", offset ?: -1)
        savedState.putInt("size", size ?: -1)
    }

    fun onRestoreState(savedState: Bundle) {
        val offset = savedState.getInt("offset")
        this.offset = if(offset == -1) null else offset

        val size = savedState.getInt("size")
        this.size = if(size == -1) null else size
    }
}