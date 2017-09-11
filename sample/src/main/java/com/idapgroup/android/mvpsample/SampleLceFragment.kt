package com.idapgroup.android.mvpsample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.idapgroup.android.mvp.impl.LcePresenterFragment

class SampleLceFragment : SampleLceMvp.View, LcePresenterFragment<SampleLceMvp.View, SampleLceMvp.Presenter>() {

    override fun showError(error: Throwable, retry: (() -> Unit)?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup): View {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreatePresenter(): SampleLceMvp.Presenter {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
//
//    override var retainPresenter = true
//
//    override fun onCreatePresenter() = SampleLcePresenter()
//
//    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup): View {
//        return inflater.inflate(R.layout.screen_sample, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        // Temp fix for overlaying screens
//        view.background = ColorDrawable(Color.WHITE)
//
//        view.findViewById(R.id.ask).setOnClickListener {
//            val question = (view.findViewById(R.id.question) as TextView).text
//            presenter.onAsk(question.toString())
//        }
//        view.findViewById(R.id.confirm).setOnClickListener { presenter.onConfirm() }
//    }
//
//    override fun goToMain() {
//        fragmentManager.popBackStack()
//    }
//
//    override fun showMessage(message: String) {
//        (view!!.findViewById(R.id.result) as TextView).text = message
//    }
//
//    override fun showLceLoad() {
//        // Because was override by SampleMvp.View interface
//        super.showLoad()
//    }
//
//    override fun showLoad() {
//        Toast.makeText(context, "Processing...", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun hideLoad() {
//        Toast.makeText(context, "Processing complete.", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun showError(error: Throwable) {
//        super.showError(error.message ?: error.toString(), { presenter.onRetry() })
//    }
}