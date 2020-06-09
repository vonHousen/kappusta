package com.vonHousen.kappusta.ui.reporting

import androidx.lifecycle.ViewModel
import com.vonHousen.kappusta.reporting.*
import java.time.LocalDate

class ReportingViewModel : ViewModel() {

    private val categories = arrayOf(
        Pair(ExpenseType.DAILY, null),
        Pair(ExpenseType.SPECIAL, null),
        Pair(null, ProfitType.SALARY),
        Pair(null, ProfitType.BONUS),
        Pair(null, ProfitType.ONE_TIME)
    )   // TODO bind it with strings.xml

    fun processNewExpenseRecord(
        reportingValue: String,
        expenseType: ExpenseType,
        date: LocalDate
    ): ExpenseRecord? {
        return if(reportingValue != "") {
            ExpenseRecord(Money(reportingValue), expenseType, date)
        } else {
            null
        }
    }

    fun processNewProfitRecord(
        reportingValue: String,
        profitType: ProfitType,
        date: LocalDate
    ): ProfitRecord? {
        return if(reportingValue != "") {
            ProfitRecord(Money(reportingValue), profitType, date)
        } else {
            null
        }
    }

    fun getReportType(position: Int): Pair<ExpenseType?, ProfitType?> = categories[position]
}