package com.vonHousen.kappusta.ui.reporting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.vonHousen.kappusta.MainActivity
import com.vonHousen.kappusta.R
import com.vonHousen.kappusta.ui.history.HistoryViewModel
import kotlinx.android.synthetic.main.fragment_reporting.*

class ReportingFragment : Fragment() {

    private lateinit var reportingViewModel: ReportingViewModel
    private lateinit var historyViewModel: HistoryViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reporting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        reportingViewModel =
            ViewModelProviders.of(this).get(ReportingViewModel::class.java)
        historyViewModel =
            ViewModelProviders.of(activity!!).get(HistoryViewModel::class.java)

        button_reporting_ok.setOnClickListener {
            reportNow()
        }

        // TODO listen to "OK" signal from keyboard
    }

    private fun reportNow() {
        val reported =
            reportingViewModel.processNewPaymentRecord(reporting_edit_text.text.toString())
        if(reported != null)
            historyViewModel.addPaymentToHistory(reported)
        (activity as MainActivity).showThingsAfterReporting()
    }

}
