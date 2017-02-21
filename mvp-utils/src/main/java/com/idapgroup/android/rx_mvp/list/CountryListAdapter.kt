//package com.idap.worldbank.screen.country.list
//
//import android.support.annotation.LayoutRes
//import android.view.View
//import com.airbnb.epoxy.EpoxyAdapter
//import com.airbnb.epoxy.EpoxyModel
//import kotlinx.android.synthetic.main.item_country.view.*
//import java.util.*
//
//fun <T> List<T>.copy(): List<T> = ArrayList(this)
//
//class CountryListAdapter(
//        val onItemClick: (Country) -> Unit
//) : EpoxyAdapter() {
//
//    val upLoad = LoadModel {
//        if(models.isEmpty()) {
//            addModel(this)
//        } else {
//            insertModelBefore(this, models.first())
//        }
//    }
//    val downLoad = LoadModel { addModel(this) }
//
//    fun addUpItemList(itemList: List<Country>) {
//        itemList.map { CountryModel(it) }
//                .let {
//                    if(models.contains(upLoad)) {
//                        it.reversed().forEach {
//                            insertModelAfter(it, upLoad)
//                        }
//                    } else {
//                        val firstModel = models.first()
//                        it.forEach {
//                            insertModelBefore(it, firstModel)
//                        }
//                    }
//                }
//    }
//
//    fun removeUpItemList(size: Int) {
//        val upIndex = models.indexOf(upLoad)
//        val deleteOffset = if(upIndex != -1) upIndex + 1 else 0
//        models.subList(deleteOffset, deleteOffset + size)
//                .copy()
//                .forEach { removeModel(it) }
//    }
//
//    fun addDownItemList(itemList: List<Country>) {
//        itemList.map { CountryModel(it) }
//                .let {
//                    if(models.contains(downLoad)) {
//                        it.forEach {
//                            insertModelBefore(it, downLoad)
//                        }
//                    } else {
//                        addModels(it)
//                    }
//                }
//    }
//
//    fun removeDownItemList(size: Int) {
//        val downIndex = models.indexOf(downLoad)
//        val deleteEnd = if(downIndex != -1) downIndex else models.size
//        val deleteOffset = deleteEnd - size
//        models.subList(deleteOffset, deleteEnd)
//                .copy()
//                .forEach { removeModel(it) }
//    }
//
//    inner class CountryModel(val country: Country) : EpoxyModel<View>() {
//
//        override fun shouldSaveViewState() = true
//
//        @LayoutRes override fun getDefaultLayout() = R.layout.item_country
//
//        override fun bind(v: View) {
//            v.comment.text = if(country.comment.isNotEmpty()) "Comment: ${country.comment}" else ""
//            v.name.text = "Country: ${country.name}"
//            v.region.text = "Region: ${country.region.name}"
//            v.setOnClickListener { onItemClick(country) }
//        }
//    }
//
//    inner class LoadModel(val showLoadModel: LoadModel.() -> Unit) : EpoxyModel<View>() {
//
//        override fun getDefaultLayout() = R.layout.item_load
//
//        override fun show(show: Boolean): EpoxyModel<View> {
//            if(show) {
//                showLoadModel()
//            } else {
//                removeModel(this)
//            }
//            return this
//        }
//    }
//}