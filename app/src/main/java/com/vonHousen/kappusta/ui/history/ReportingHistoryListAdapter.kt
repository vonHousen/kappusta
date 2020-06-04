package com.vonHousen.kappusta.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vonHousen.kappusta.paymentRecord.PaymentRecord


class ReportingHistoryListAdapter(private val list: List<PaymentRecord>)
    : RecyclerView.Adapter<ReportingHistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportingHistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ReportingHistoryViewHolder(
            inflater,
            parent
        )
    }

    override fun onBindViewHolder(holder: ReportingHistoryViewHolder, position: Int) {
        val payment: PaymentRecord = list[position]
        holder.bind(payment)
    }

    override fun getItemCount(): Int = list.size
}