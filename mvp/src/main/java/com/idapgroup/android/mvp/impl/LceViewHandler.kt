package com.idapgroup.android.mvp.impl

import android.app.Activity
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.idapgroup.android.mvp.LceView
import com.idapgroup.android.mvp.R
import com.idapgroup.android.mvp.RawLceView
import com.idapgroup.android.mvp.impl.DefaultLceViewCreator.Layout

private val CONTAINER_LAYOUT_RES = R.layout.lce_base_container

class LceViewHandler (
        var lceViewCreator: LceViewCreator? = null
) : LceView, RawLceView {

    constructor(@LayoutRes contentLayoutRes: Int): this(createViewCreator(contentLayoutRes))

    constructor(createContentView: ViewCreator,
                createLoadView: ViewCreator = Layout.CREATE_LOAD_VIEW,
                createErrorView: ViewCreator = Layout.CREATE_ERROR_VIEW
    ): this(SimpleLceViewCreator(createContentView, createLoadView, createErrorView))

    var loadView: View? = null
        private set
    var contentView: View? = null
        private set

    var errorContainerView: View? = null
        private set
    var errorMessageView: TextView? = null
        private set
    var errorRetryView: View? = null

    fun createAndInitView(inflater: LayoutInflater, rootContainer: ViewGroup?, creator: LceViewCreator): View {
        val container = inflater.inflate(CONTAINER_LAYOUT_RES, rootContainer, false)
        return fillAndInitView(inflater, container as ViewGroup, creator)
    }

    fun fillAndInitView(container: ViewGroup, creator: LceViewCreator): View {
        val inflater = LayoutInflater.from(container.context)
        return fillAndInitView(inflater, container, creator)
    }

    private fun fillAndInitView(inflater: LayoutInflater, container: ViewGroup, creator: LceViewCreator): View {
        val childCount = container.childCount
        return container.apply {
            // Load
            loadView = creator.onCreateLoadView(inflater, this)
            addView(loadView)
            checkContainerState(this, childCount + 1, "onCreateLoadView")
            // Error
            errorContainerView = creator.onCreateErrorView(inflater, this)
            errorMessageView = errorContainerView!!.findViewById(R.id.errorMessage) as TextView?
            errorRetryView = errorContainerView!!.findViewById(R.id.errorRetry)
            addView(errorContainerView)
            checkContainerState(this, childCount + 2, "onCreateErrorView")
            // Content
            contentView = creator.onCreateContentView(inflater, this)
            addView(contentView)
            checkContainerState(this, childCount + 3, "onCreateContentView")
            // Show default state
            showContent()
        }
    }

    private fun checkContainerState(container: ViewGroup, requisiteChildCount: Int, name: String) {
        if(container.childCount != requisiteChildCount) {
            throw IllegalStateException("'$name()' modified container child view set")
        }
    }

    fun initView(rootView: View) : View {
        loadView = rootView.findViewById(R.id.load)
        contentView = rootView.findViewById(R.id.content) as ViewGroup?
        errorContainerView = rootView.findViewById(R.id.errorContainer)
        errorMessageView = rootView.findViewById(R.id.errorMessage) as TextView?
        errorRetryView = rootView.findViewById(R.id.errorRetry)
        // Set default state
        showContent()
        return rootView
    }

    fun resetView() {
        loadView = null
        contentView = null
        errorContainerView = null
        errorMessageView = null
        errorRetryView = null
    }

    /** Show loading view  */
    override fun showLoad() {
        hideAll()
        loadView!!.visibility = View.VISIBLE
    }

    /** Show base contentView container  */
    override fun showContent() {
        hideAll()
        contentView!!.visibility = View.VISIBLE
    }

    /** Show error view  */
    override fun showError(error: Throwable, retry: (() -> Unit)?) {
        showError(error.message ?: error.toString(), retry)
    }

    /** Show error view  */
    override fun showError(errorMessage: String, retry: (() -> Unit)?) {
        hideAll()
        errorContainerView!!.visibility = View.VISIBLE
        errorMessageView?.text = errorMessage

        if(errorRetryView != null) {
            if(retry != null) {
                errorRetryView!!.visibility = View.VISIBLE
                errorRetryView!!.setOnClickListener { retry() }
            } else {
                errorRetryView!!.visibility = View.GONE
                errorRetryView!!.setOnClickListener(null)
            }
        }
    }

    private fun hideAll() {
        loadView!!.visibility = View.GONE
        contentView!!.visibility = View.GONE
        errorContainerView?.visibility = View.GONE
    }
}

fun Activity.setContentView(handler: LceViewHandler, creator: LceViewCreator? = null): View {
    setContentView(CONTAINER_LAYOUT_RES)
    val container = findViewById(R.id.lce_container) as ViewGroup
    return handler.fillAndInitView(container, creator ?: handler.lceViewCreator!!)
}
