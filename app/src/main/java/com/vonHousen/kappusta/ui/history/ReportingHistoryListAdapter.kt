package com.vonHousen.kappusta.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vonHousen.kappusta.reporting.ExpenseRecord


class ReportingHistoryListAdapter(private val list: List<ExpenseRecord>)
    : RecyclerView.Adapter<ReportingHistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportingHistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ReportingHistoryViewHolder(
            inflater,
            parent
        )
    }

    override fun onBindViewHolder(holder: ReportingHistoryViewHolder, position: Int) {
        val expense: ExpenseRecord = list[position]
        holder.bind(expense)
    }

    override fun getItemCount(): Int = list.size
}