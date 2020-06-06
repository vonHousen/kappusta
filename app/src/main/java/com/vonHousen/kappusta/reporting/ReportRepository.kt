package com.vonHousen.kappusta.reporting

import com.vonHousen.kappusta.MainActivity
import com.vonHousen.kappusta.db.ReportDAO
import com.vonHousen.kappusta.db.ExpenseEntity

object ReportRepository {
    private val reportDAO: ReportDAO = MainActivity.db.reportDAO()

    fun add(expenseRecord: ExpenseRecord) {
        reportDAO.insertReport(ExpenseEntity(expenseRecord))
    }

    fun getAll(): List<ExpenseRecord> {
        val expensesTmp = mutableListOf<ExpenseRecord>()
        val loadedExpenses = reportDAO.allExpenses
        if (loadedExpenses != null) {
            for (expense in loadedExpenses) {
                expensesTmp.add(ExpenseRecord(expense))
            }
        }
        return expensesTmp
    }
}