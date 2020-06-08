package com.vonHousen.kappusta.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vonHousen.kappusta.reporting.*

class HistoryViewModel : ViewModel() {

    private val repo = ReportRepository

    private var reportList = repo.getFullReport().toMutableList()
    private val _reportList = MutableLiveData<List<ReportRecord>>().apply {
        value = reportList
    }
    val reports: LiveData<List<ReportRecord>>
        get() = _reportList

    private val _summaryReport = MutableLiveData<SummaryReport>().apply {
        value = repo.getSummaryReport()
    }
    val summaryReport: LiveData<SummaryReport>
        get() = _summaryReport

    fun addExpenseToHistory(newExpense: ExpenseRecord) {
        val addedID = repo.addExpense(newExpense)
        val newReport = ReportRecord(
            newExpense.getDate(),
            -newExpense.getHowMuch(),
            newExpense.getExpenseType().toString(),
            addedID
        )
        reportList.add(newReport)
        reportList.sortByDescending { it.DATE }
        _summaryReport.value = repo.getSummaryReport()      // TODO you don't need to query db again
    }

    fun addProfitToHistory(newProfit: ProfitRecord) {
        val addedID = repo.addProfit(newProfit)
        val newReport = ReportRecord(
            newProfit.getDate(),
            newProfit.getHowMuch(),
            newProfit.getProfitType().toString(),
            addedID
        )
        reportList.add(newReport)
        reportList.sortByDescending { it.DATE }
        _summaryReport.value = repo.getSummaryReport()      // TODO you don't need to query db again
    }

    fun notifyReportRemoved(position: Int) {
        val removedItem = reportList.removeAt(position)
        repo.removeReport(removedItem)
        _summaryReport.value = repo.getSummaryReport()      // TODO you don't need to query db again
    }
}