package com.vonHousen.kappusta.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vonHousen.kappusta.R
import com.vonHousen.kappusta.reporting.ExpenseRecord

class ReportingHistoryViewHolder(inflater: LayoutInflater, parent: ViewGroup)
    : RecyclerView.ViewHolder(
        inflater.inflate(R.layout.reporting_history_item_row, parent, false)
    ) {
    private var categoryField: TextView? = null
    private var dateField: TextView? = null
    private var howMuchField: TextView? = null

    init {
        categoryField = itemView.findViewById(R.id.reporting_history_category)
        dateField = itemView.findViewById(R.id.reporting_history_date)
        howMuchField = itemView.findViewById(R.id.reporting_history_how_much)
    }

    fun bind(expense: ExpenseRecord) {
        categoryField?.text = expense.getExpenseTypeString()
        dateField?.text = expense.getDateString()
        howMuchField?.text = expense.getHowMuchString()
    }
}