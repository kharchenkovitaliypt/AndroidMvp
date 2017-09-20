package com.idapgroup.android.mvp.impl

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.idapgroup.android.mvp.R

typealias ViewCreator = (inflater: LayoutInflater, container: ViewGroup) -> View

interface LceViewCreator {
    fun onCreateLoadView(inflater: LayoutInflater, container: ViewGroup) : View
    fun onCreateErrorView(inflater: LayoutInflater, container: ViewGroup) : View
    fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup): View
}

open class SimpleLceViewCreator(
        val createContentView: ViewCreator,
        val createLoadView: ViewCreator = Layout.CREATE_LOAD_VIEW,
        val createErrorView: ViewCreator = Layout.CREATE_ERROR_VIEW
): LceViewCreator {

    object Layout {
        val LOAD = R.layout.lce_base_load
        val ERROR = R.layout.lce_base_error
        val CREATE_LOAD_VIEW = toViewCreator(Layout.LOAD)
        val CREATE_ERROR_VIEW = toViewCreator(Layout.ERROR)
    }

    constructor(@LayoutRes contentRes: Int,
                @LayoutRes loadRes: Int = Layout.LOAD,
                @LayoutRes errorRes: Int = Layout.ERROR
    ): this(
            toViewCreator(contentRes),
            toViewCreator(loadRes),
            toViewCreator(errorRes)
    )

    override fun onCreateLoadView(inflater: LayoutInflater, container: ViewGroup) : View {
        return createLoadView(inflater, container)
    }

    override fun onCreateErrorView(inflater: LayoutInflater, container: ViewGroup) : View {
        return createErrorView(inflater, container)
    }

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup): View {
        return createContentView(inflater, container)
    }
}

fun toViewCreator(@LayoutRes layoutRes: Int): ViewCreator {
    return { inflater, container ->
        inflater.inflate(layoutRes, container, false)
    }
}

