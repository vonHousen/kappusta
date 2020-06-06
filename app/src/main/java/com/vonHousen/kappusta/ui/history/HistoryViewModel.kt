package com.vonHousen.kappusta.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vonHousen.kappusta.reporting.ExpenseRecord
import com.vonHousen.kappusta.reporting.ReportRepository
import com.vonHousen.kappusta.reporting.ReportRecord

class HistoryViewModel : ViewModel() {

    private val repo = ReportRepository
    private var reportList = repo.getFullReport().toMutableList()
    private val _reportList = MutableLiveData<List<ReportRecord>>().apply {
        value = reportList
    }
    val reports: LiveData<List<ReportRecord>>
        get() = _reportList

    fun addExpenseToHistory(newExpense: ExpenseRecord) {
        val newReport = ReportRecord(
            newExpense.getDate(),
            -newExpense.getHowMuch(),
            newExpense.getExpenseTypeString()
        )
        reportList.add(newReport)
        repo.addExpense(newExpense)
    }
}