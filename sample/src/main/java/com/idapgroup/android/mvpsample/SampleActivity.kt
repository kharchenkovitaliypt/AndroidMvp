package com.idapgroup.android.mvpsample

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.TextView
import com.idapgroup.android.mvp.impl.BasePresenterActivity

class SampleActivity : BasePresenterActivity<SampleMvp.View, SampleMvp.Presenter>(), SampleMvp.View {

    var loadDialog: ProgressDialog? = null

    override fun onCreatePresenter() = SamplePresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_sample)

        findViewById(R.id.ask).setOnClickListener {
            val question = (findViewById(R.id.question) as TextView).text
            presenter.onAsk(question.toString())
        }
        findViewById(R.id.confirm).setOnClickListener { presenter.onConfirm() }

        if (savedInstanceState != null) {
            if(savedInstanceState.getBoolean("load_dialog_shown", false)) {
                showLoad()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("load_dialog_shown", loadDialog != null)
    }

    override fun goToMain() {
        finish()
    }

    override fun showMessage(message: String) {
        (findViewById(R.id.result) as TextView).text = message
    }

    override fun showLoad() {
        val loadDialog = ProgressDialog(this)
        loadDialog.setMessage("Processing...")
        loadDialog.isIndeterminate = true
        loadDialog.setCancelable(false)
        loadDialog.setCanceledOnTouchOutside(false)
        loadDialog.show()
        this.loadDialog = loadDialog
    }

    override fun hideLoad() {
        loadDialog!!.hide()
        loadDialog = null
    }

    override fun showError(error: Throwable) {
        AlertDialog.Builder(this)
                .setMessage(error.toString())
                .setPositiveButton(android.R.string.ok, { _, _ ->  })
                .show()
    }
}
