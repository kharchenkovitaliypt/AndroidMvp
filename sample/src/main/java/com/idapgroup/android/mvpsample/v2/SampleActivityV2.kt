package com.idapgroup.android.mvpsample.v2

import android.app.Activity
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

    private lateinit var presenter: SampleMvp.Presenter

    val presenterView =  SampleFragmentView(this)

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        onInitView()

        findViewById(R.id.ask).setOnClickListener {
            val question = (findViewById(R.id.question) as TextView).text
            presenter.onAsk(question.toString())
        }
        findViewById(R.id.confirm).setOnClickListener { presenter.onConfirm() }
        findViewById(R.id.takeUntilDetachView).setOnClickListener {
            presenter.takeUntilDetachView()
        }

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
        outState.putBoolean("load_dialog_shown", presenterView.loadDialog != null)
    }
}

class SampleFragmentView(val activity: Activity) : SampleMvp.View {

    var loadDialog: ProgressDialog? = null

    override fun goToMain() = activity.finish()

    override fun showMessage(message: String) {
        (activity.findViewById(R.id.result) as TextView).text = message
    }

    override fun showLoad() {
        loadDialog = ProgressDialog(activity).apply {
            setMessage("Processing...")
            isIndeterminate = true
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            show()
        }
    }

    override fun hideLoad() {
        loadDialog!!.hide()
        loadDialog = null
    }

    override fun showError(error: Throwable) {
        AlertDialog.Builder(activity)
                .setMessage(error.toString())
                .setPositiveButton(android.R.string.ok, { _, _ -> })
                .show()
    }
}