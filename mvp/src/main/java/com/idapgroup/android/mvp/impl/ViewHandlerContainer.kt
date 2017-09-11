package com.idapgroup.android.mvp.impl

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import com.idapgroup.android.mvp.R

private val STATE_KEY_PARENT = "parentState"
private val STATE_KEY_INPUT_FIELD_LIST = "inputFieldListState"

open class ViewHandlerContainer
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0)
    : FrameLayout(context, attrs, defStyle) {

    override fun onSaveInstanceState(): Parcelable {
        val generalState = Bundle()
        generalState.putParcelable(STATE_KEY_PARENT, super.onSaveInstanceState())

        val inputFieldListState = Bundle()
        viewHandlerList?.forEach {
            val key = it.id.toString()
            inputFieldListState.putParcelable(key, it.onSaveInstanceState())
        }
        generalState.putBundle(STATE_KEY_INPUT_FIELD_LIST, inputFieldListState)
        return generalState
    }

    override fun onRestoreInstanceState(generalState: Parcelable) {
        generalState as Bundle
        super.onRestoreInstanceState(generalState.getParcelable<Parcelable>(STATE_KEY_PARENT))

        generalState.getBundle(STATE_KEY_INPUT_FIELD_LIST)?.let { state ->
            viewHandlerList?.forEach { vh ->
                val key = vh.id.toString()
                state.getParcelable<Bundle>(key)?.let {
                    vh.onRestoreInstanceState(it)
                }
            }
        }
    }
}

fun ViewGroup.attachViewHandlers(list: List<ViewHandler>) {
    val inflater = LayoutInflater.from(context)
    list.forEach {
        it.onCreateView(inflater, this)
    }

    val fullList = viewHandlerList as? MutableList<ViewHandler> ?: mutableListOf()
    fullList.addAll(list)
    setTag(R.id.viewHandlerList, fullList)
}

fun ViewGroup.detachViewHandlers() {
    viewHandlerList?.forEach { vh ->
        vh.baseView?.let {
            vh.onDestroyView()
            removeView(it)
        }
    }
    (viewHandlerList as? MutableList<ViewHandler>)?.clear()
}

val ViewGroup.viewHandlerList: List<ViewHandler>? get() {
    @Suppress("UNCHECKED_CAST")
    return getTag(R.id.viewHandlerList) as? List<ViewHandler>
}
