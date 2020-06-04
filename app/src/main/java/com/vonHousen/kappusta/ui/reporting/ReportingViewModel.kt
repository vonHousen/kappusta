package com.vonHousen.kappusta.ui.reporting

import androidx.lifecycle.ViewModel
import com.vonHousen.kappusta.paymentRecord.PaymentRecord

class ReportingViewModel : ViewModel() {

    fun processNewPaymentRecord(reportingValue: String): PaymentRecord? {
        return if(reportingValue != "") {
            PaymentRecord(reportingValue.toDouble())
        } else {
            null
        }
    }
}