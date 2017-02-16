package com.idapgroup.android.mvp.impl

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.widget.LinearLayoutCompat
import android.util.AttributeSet
import android.view.LayoutInflater

open class ViewHandlerContainer : LinearLayoutCompat {

    private val STATE_KEY_PARENT = "parentState"
    private val STATE_KEY_INPUT_FIELD_LIST = "inputFieldListState"

    private var viewHandlerList: List<ViewHandler>? = null

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        orientation = VERTICAL
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        orientation = VERTICAL
    }

    override fun onSaveInstanceState(): Parcelable {
        val generalState = Bundle()
        generalState.putParcelable(STATE_KEY_PARENT, super.onSaveInstanceState())

        val inputFieldListState = Bundle()
        for (viewHandler in viewHandlerList!!) {
            val key = viewHandler.id.toString()
            inputFieldListState.putParcelable(key, viewHandler.onSaveInstanceState())
        }
        generalState.putBundle(STATE_KEY_INPUT_FIELD_LIST, inputFieldListState)

        return generalState
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val generalState = state as Bundle
        super.onRestoreInstanceState(generalState.getParcelable<Parcelable>(STATE_KEY_PARENT))

        val viewHandlerListState = generalState.getBundle(STATE_KEY_INPUT_FIELD_LIST)
        if (viewHandlerListState != null) {
            for (viewHandler in viewHandlerList!!) {
                val key = viewHandler.id.toString()
                val viewHandlerState = viewHandlerListState.getParcelable<Bundle>(key)
                if (viewHandlerState != null) {
                    viewHandler.onRestoreInstanceState(viewHandlerState)
                }
            }
        }
    }

    fun setViewHandlerList(viewHandlerList: List<ViewHandler>) {
        this.viewHandlerList = viewHandlerList

        val inflater = LayoutInflater.from(context)
        for(vh in viewHandlerList) {
            if(vh.visible) vh.onCreateView(inflater, this)
        }
    }

    fun releaseInputFieldList() {
        for (viewHandler in this.viewHandlerList!!) {
            val rootView = viewHandler.rootView
            if (rootView != null) {
                viewHandler.onDestroyView()
                removeView(rootView)
            }
        }
        this.viewHandlerList = null
    }
}
