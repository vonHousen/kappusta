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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.vonHousen.kappusta.reporting.ReportRecord


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
        historyViewModel.reports.observe(viewLifecycleOwner, Observer {
            updateRecyclerView(historyViewModel.reports.value)
        })

        configureSwiping()
    }

    private fun updateRecyclerView(reportingHistoryList: List<ReportRecord>?) {
        if (reportingHistoryList != null) {
            val customAdapter =
                ReportingHistoryListAdapter(reportingHistoryList.toMutableList(), historyViewModel)
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

    private fun configureSwiping() {
        val swipeHandler = object : SwipeToDeleteCallback(context!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = recycler_view_reporting_history.adapter as ReportingHistoryListAdapter
                adapter.removeAt(viewHolder.adapterPosition)
            }
        }
        ItemTouchHelper(swipeHandler).attachToRecyclerView(recycler_view_reporting_history)
    }
}
