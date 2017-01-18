package com.idapgroup.android.rx_mvp.load

import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.idapgroup.android.mvp.LceView
import com.idapgroup.android.mvp.R

class LceViewHandler(view: View) : LceView {

    var progressView: View? = null
        private set
    var contentView: ViewGroup? = null
        private set
    var errorContainerView: View? = null
        private set
    var errorMessageView: TextView? = null
        private set
    var errorRetryView: View? = null

    init {
        initView(view)
    }

    fun initView(rootView: View) {
        progressView = rootView.findViewById(R.id.progress)
        contentView = rootView.findViewById(R.id.content) as ViewGroup?
        errorContainerView = rootView.findViewById(R.id.error_container)
        errorMessageView = rootView.findViewById(R.id.error_message) as TextView?
        errorRetryView = rootView.findViewById(R.id.error_retry)
        // Set default state
        showContent()
    }

    fun resetView() {
        progressView = null
        contentView = null
        errorContainerView = null
        errorMessageView = null
        errorRetryView = null
    }

    /** Show loading view  */
    override fun showLoad() {
        hideAll()
        progressView!!.visibility = View.VISIBLE
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
        progressView!!.visibility = View.GONE
        contentView!!.visibility = View.GONE
        errorContainerView?.visibility = View.GONE
    }
}
