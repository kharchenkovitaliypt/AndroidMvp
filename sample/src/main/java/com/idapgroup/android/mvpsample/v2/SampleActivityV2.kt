package com.idapgroup.android.mvpsample.v2

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.idapgroup.android.mvp.impl.v2.attachPresenter
import com.idapgroup.android.mvpsample.R
import com.idapgroup.android.mvpsample.SampleMvp
import com.idapgroup.android.mvpsample.SamplePresenter

open class SampleActivityV2 : AppCompatActivity() {

    private var loadDialog: ProgressDialog? = null

    private lateinit var presenter: SampleMvp.Presenter

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        onInitView()

        findViewById(R.id.ask).setOnClickListener {
            val question = (findViewById(R.id.question) as TextView).text
            presenter.onAsk(question.toString())
        }
        findViewById(R.id.confirm).setOnClickListener { presenter.onConfirm() }

        presenter = attachPresenter(this, presenterView, ::SamplePresenter, savedState, true)

        savedState?.let {
            if(it.getBoolean("load_dialog_shown", false)) {
                presenterView.showLoad()
            }
        }
    }

    open fun onInitView() {
        setContentView(R.layout.screen_sample)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("load_dialog_shown", loadDialog != null)
    }

    private val presenterView = object : SampleMvp.View {

        override fun goToMain() = finish()

        override fun showMessage(message: String) {
            (findViewById(R.id.result) as TextView).text = message
        }

        override fun showLoad() {
            val loadDialog = ProgressDialog(this@SampleActivityV2)
            loadDialog.setMessage("Processing...")
            loadDialog.isIndeterminate = true
            loadDialog.setCancelable(false)
            loadDialog.setCanceledOnTouchOutside(false)
            loadDialog.show()
            this@SampleActivityV2.loadDialog = loadDialog
        }

        override fun hideLoad() {
            loadDialog!!.hide()
            loadDialog = null
        }

        override fun showError(error: Throwable) {
            AlertDialog.Builder(this@SampleActivityV2)
                    .setMessage(error.toString())
                    .setPositiveButton(android.R.string.ok, { _, _ -> })
                    .show()
        }
    }
}