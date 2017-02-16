//package com.idap.worldbank.screen.country.list
//
//import android.support.v7.widget.LinearLayoutManager
//import android.support.v7.widget.RecyclerView
//
//class ScrollHandler(
//        val layoutManager: LinearLayoutManager,
//        var onStart: (() -> Unit)? = null,
//        var onEnd: (() -> Unit)? = null
//) : RecyclerView.OnScrollListener() {
//
//    private var startNotified = false
//    private var endNotified = false
//
//    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
//        //Log.d("ScrollHandler", "onScrolled() dx: $dx, dy: $dy")
//
//        val firstPos = layoutManager.findFirstVisibleItemPosition()
//        if(firstPos == 0) {
//            if(!startNotified) {
//                startNotified = true
//                onStart?.invoke()
//                return
//            }
//        } else {
//            startNotified = false
//        }
//
//        val itemCount = layoutManager.itemCount
//        val lastPos = layoutManager.findLastVisibleItemPosition()
//        if(itemCount - 1 == lastPos) {
//            if(!endNotified) {
//                endNotified = true
//                onEnd?.invoke()
//                return
//            }
//        } else {
//            endNotified = false
//        }
//        //Log.d("ScrollHandler", "onScrolled() firstPos: $firstPos, lastPos: $lastPos")
//    }
//}