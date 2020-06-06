package com.vonHousen.kappusta.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vonHousen.kappusta.reporting.ExpenseRecord
import com.vonHousen.kappusta.reporting.ReportRepository

class HistoryViewModel : ViewModel() {

    private val repo = ReportRepository
    private var expensesList = repo.getAll().toMutableList()
    private val _expensesList = MutableLiveData<List<ExpenseRecord>>().apply {
        value = expensesList
    }
    val payments: LiveData<List<ExpenseRecord>>
        get() = _expensesList

    fun addExpenseToHistory(newExpense: ExpenseRecord) {
        expensesList.add(newExpense)
        repo.add(newExpense)
    }
}