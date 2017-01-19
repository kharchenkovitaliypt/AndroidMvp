package com.idapgroup.android.rx_mvp.load

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.idapgroup.android.mvp.LceView
import com.idapgroup.android.mvp.R

class LceViewHandler : LceView {

    interface LceComponentViewCreator {
        fun onCreateLoadView(inflater: LayoutInflater, container: ViewGroup) : View
        fun onCreateErrorView(inflater: LayoutInflater, container: ViewGroup) : View
        fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup): View
    }

    companion object {
        val BASE_CONTAINER_ID = R.layout.lce_base_container
    }

    var loadView: View? = null
        private set
    var contentView: ViewGroup? = null
        private set
    var errorContainerView: View? = null
        private set
    var errorMessageView: TextView? = null
        private set
    var errorRetryView: View? = null

    fun createAndInitView(inflater: LayoutInflater, rootContainer: ViewGroup?, creator: LceComponentViewCreator): View {
        var container = inflater.inflate(BASE_CONTAINER_ID, rootContainer, false)
        return initView(inflater, container as ViewGroup, creator)
    }

    fun initView(inflater: LayoutInflater, container: ViewGroup, creator: LceComponentViewCreator): View {
        val childCount = container.childCount
        return container.apply {
            // Load
            addView(creator.onCreateLoadView(inflater, this).apply {
                id = R.id.load
            })
            checkContainerState(this, childCount + 1, "onCreateLoadView")
            // Error
            addView(creator.onCreateErrorView(inflater, this).apply {
                id = R.id.errorContainer
            })
            checkContainerState(this, childCount + 2, "onCreateErrorView")
            // Content
            addView(creator.onCreateContentView(inflater, this).apply {
                id = R.id.content
            })
            checkContainerState(this, childCount + 3, "onCreateContentView")

            initView(this)
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
        errorRetryView?.setOnClickListener(
                if(retry == null) null else  View.OnClickListener { retry() }
        )
    }

    private fun hideAll() {
        loadView!!.visibility = View.GONE
        contentView!!.visibility = View.GONE
        errorContainerView?.visibility = View.GONE
    }
}
