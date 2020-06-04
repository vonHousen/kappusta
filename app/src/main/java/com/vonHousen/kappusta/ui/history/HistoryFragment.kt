package com.vonHousen.kappusta.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.vonHousen.kappusta.R
import kotlinx.android.synthetic.main.fragment_history.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.vonHousen.kappusta.paymentRecord.PaymentRecord


class HistoryFragment : Fragment() {

    private lateinit var historyViewModel: HistoryViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // RecyclerView node initialized here
        historyViewModel =
            ViewModelProviders.of(activity!!).get(HistoryViewModel::class.java)
        historyViewModel.payments.observe(viewLifecycleOwner, Observer {
            updateRecyclerView(historyViewModel.payments.value)
        })
    }

    private fun updateRecyclerView(reportingHistoryList: List<PaymentRecord>?) {
        if (reportingHistoryList != null) {
            val customAdapter = ReportingHistoryListAdapter(reportingHistoryList)
            recycler_view_reporting_history.apply {
                // set a LinearLayoutManager to handle Android
                // RecyclerView behavior
                layoutManager = LinearLayoutManager(activity)
                // set the custom adapter to the RecyclerView
                adapter = customAdapter
            }
        }
        // TODO else show "empty" textbox or something
    }
}
