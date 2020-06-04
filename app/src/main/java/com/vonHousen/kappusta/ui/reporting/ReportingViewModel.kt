package com.vonHousen.kappusta.ui.reporting

import androidx.lifecycle.ViewModel
import com.vonHousen.kappusta.paymentRecord.Category
import com.vonHousen.kappusta.paymentRecord.PaymentRecord
import java.time.LocalDate

class ReportingViewModel : ViewModel() {

    val categories = arrayOf(
        Category.DAILY,
        Category.SPECIAL
    )

    fun processNewPaymentRecord(
        reportingValue: String,
        category: Category,
        date: LocalDate
    ): PaymentRecord? {
        return if(reportingValue != "") {
            PaymentRecord(reportingValue.toDouble(), date, category)
        } else {
            null
        }
    }
}