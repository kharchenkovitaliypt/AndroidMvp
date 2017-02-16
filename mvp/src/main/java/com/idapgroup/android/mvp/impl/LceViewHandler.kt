package com.idapgroup.android.mvp.impl

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.idapgroup.android.mvp.LceView
import com.idapgroup.android.mvp.R

interface LceViewCreator {
    fun onCreateLoadView(inflater: LayoutInflater, container: ViewGroup) : View
    fun onCreateErrorView(inflater: LayoutInflater, container: ViewGroup) : View
    fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup): View
}

class DefaultLceViewCreator(
        val contentViewCreator: ((LayoutInflater, ViewGroup) -> View)? = null
): LceViewCreator {

    object Layout {
        val ERROR = R.layout.lce_base_error
        val LOAD = R.layout.lce_base_error
    }

    override fun onCreateErrorView(inflater: LayoutInflater, container: ViewGroup) : View {
        return inflater.inflate(Layout.ERROR, container, false)
    }

    override fun onCreateLoadView(inflater: LayoutInflater, container: ViewGroup) : View {
        return inflater.inflate(Layout.LOAD, container, false)
    }

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup): View {
        return contentViewCreator!!.invoke(inflater, container)
    }
}

class LceViewHandler : LceView {

    companion object {
        val BASE_CONTAINER_ID = R.layout.lce_base_container
    }

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
        var container = inflater.inflate(BASE_CONTAINER_ID, rootContainer, false)
        return initView(inflater, container as ViewGroup, creator)
    }

    fun initView(inflater: LayoutInflater, container: ViewGroup, creator: LceViewCreator): View {
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
            // Set default state
            showContent()
        }
    }

    fun checkContainerState(container: ViewGroup, requisiteChildCount: Int, name: String) {
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
