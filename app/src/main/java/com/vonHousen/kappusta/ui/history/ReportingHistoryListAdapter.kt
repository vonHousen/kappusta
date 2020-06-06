package com.vonHousen.kappusta.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vonHousen.kappusta.reporting.ReportRecord


class ReportingHistoryListAdapter(private val list: List<ReportRecord>)
    : RecyclerView.Adapter<ReportingHistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportingHistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ReportingHistoryViewHolder(
            inflater,
            parent
        )
    }

    override fun onBindViewHolder(holder: ReportingHistoryViewHolder, position: Int) {
        val report = list[position]
        holder.bind(report)
    }

    override fun getItemCount(): Int = list.size
}