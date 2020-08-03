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
            ExpenseRecord(
                howMuch = Money(reportingValue),
                expenseType = expenseType,
                expenseTagID = null,        // TODO bind it with UI
                date = date,
                comment = null              // TODO bind it with UI
            )
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
            ProfitRecord(
                worth = Money(reportingValue),
                profitType = profitType,
                profitTagID = null,         // TODO bind it with UI
                date = date,
                comment = null              // TODO bind it with UI
            )
        } else {
            null
        }
    }

    fun getReportType(position: Int): Pair<ExpenseType?, ProfitType?> = categories[position]
}