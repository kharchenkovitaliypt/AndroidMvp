package com.idapgroup.android.mvp.impl

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.idapgroup.android.mvp.R
import com.idapgroup.android.mvp.impl.DefaultLceViewCreator.Layout

typealias ViewCreator = (inflater: LayoutInflater, container: ViewGroup) -> View

interface LceViewCreator {
    fun onCreateLoadView(inflater: LayoutInflater, container: ViewGroup) : View
    fun onCreateErrorView(inflater: LayoutInflater, container: ViewGroup) : View
    fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup): View
}

fun createLceViewCreator(
        @LayoutRes contentRes: Int,
        @LayoutRes loadRes: Int = Layout.LOAD,
        @LayoutRes errorRes: Int = Layout.ERROR
) = SimpleLceViewCreator(
        createViewCreator(contentRes),
        createViewCreator(loadRes),
        createViewCreator(errorRes)
)

open class SimpleLceViewCreator(
        val createContentView: ViewCreator,
        val createLoadView: ViewCreator,
        val createErrorView: ViewCreator
): LceViewCreator {

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

open class DefaultLceViewCreator(
        createContentView: ViewCreator
): SimpleLceViewCreator(createContentView, Layout.CREATE_LOAD_VIEW, Layout.CREATE_ERROR_VIEW) {

    object Layout {
        val LOAD = R.layout.lce_base_load
        val ERROR = R.layout.lce_base_error
        val CREATE_LOAD_VIEW = createViewCreator(Layout.LOAD)
        val CREATE_ERROR_VIEW = createViewCreator(Layout.ERROR)
    }
}

fun createViewCreator(@LayoutRes layoutRes: Int): ViewCreator {
    return { inflater, container ->
        inflater.inflate(layoutRes, container, false)
    }
}

