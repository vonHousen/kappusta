package com.vonHousen.kappusta.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vonHousen.kappusta.paymentRecord.PaymentRecord

class HistoryViewModel : ViewModel() {

    private var paymentList = mutableListOf<PaymentRecord>(
    )
    private val _paymentList = MutableLiveData<List<PaymentRecord>>().apply {
        value = paymentList
    }
    val payments: LiveData<List<PaymentRecord>>
        get() = _paymentList

    fun addPaymentToHistory(newPayment: PaymentRecord) {
        paymentList.add(newPayment)
    }
}