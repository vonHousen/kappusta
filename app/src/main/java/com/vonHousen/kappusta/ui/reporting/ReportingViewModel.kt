package com.vonHousen.kappusta.ui.reporting

import androidx.lifecycle.ViewModel
import com.vonHousen.kappusta.reporting.ExpenseType
import com.vonHousen.kappusta.reporting.ExpenseRecord
import java.time.LocalDate

class ReportingViewModel : ViewModel() {

    val categories = arrayOf(
        ExpenseType.DAILY,
        ExpenseType.SPECIAL
    )

    fun processNewPaymentRecord(
        reportingValue: String,
        expenseType: ExpenseType,
        date: LocalDate
    ): ExpenseRecord? {
        return if(reportingValue != "") {
            ExpenseRecord(reportingValue.toDouble(), expenseType, date)
        } else {
            null
        }
    }
}