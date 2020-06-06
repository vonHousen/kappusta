package com.vonHousen.kappusta.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vonHousen.kappusta.reporting.ExpenseRecord
import com.vonHousen.kappusta.reporting.ProfitRecord
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
            newExpense.getExpenseType().toString()
        )
        reportList.add(newReport)
        repo.addExpense(newExpense)
    }

    fun addProfitToHistory(newProfit: ProfitRecord) {
        val newReport = ReportRecord(
            newProfit.getDate(),
            newProfit.getHowMuch(),
            newProfit.getProfitType().toString()
        )
        reportList.add(newReport)
        repo.addProfit(newProfit)
    }

    fun notifyReportRemoved(position: Int) {
        val removedItem = reportList.removeAt(position)
        repo.removeReport(removedItem)
    }
}