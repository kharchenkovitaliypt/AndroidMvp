package com.idapgroup.android.mvpsample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.idapgroup.android.mvp.BasePresenterFragment

class SampleFragment : SampleMvp.View, BasePresenterFragment<SampleMvp.View, SampleMvp.Presenter>() {

    override fun createPresenter() = SamplePresenter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.screen_sample, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById(R.id.ask).setOnClickListener {
            val question = (view.findViewById(R.id.question) as TextView).text
            getPresenter().onAsk(question.toString())
        }
        view.findViewById(R.id.confirm).setOnClickListener { getPresenter().onConfirm() }
    }

    override fun goToMain() {
        fragmentManager.popBackStack()
    }

    override fun showMessage(message: String) {
        (view!!.findViewById(R.id.result) as TextView).text = message
    }

    override fun showLoad() {
        Toast.makeText(context, "Processing...", Toast.LENGTH_SHORT).show()
    }

    override fun hideLoad() {
        Toast.makeText(context, "Processing complete.", Toast.LENGTH_SHORT).show()
    }

    override fun showError(error: Throwable) {
        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()
    }
}