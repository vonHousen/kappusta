package com.vonHousen.kappusta.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vonHousen.kappusta.payment.PaymentRecord
import com.vonHousen.kappusta.payment.PaymentRepository

class HistoryViewModel : ViewModel() {

    private val repo = PaymentRepository
    private var paymentList = repo.getAll().toMutableList()
    private val _paymentList = MutableLiveData<List<PaymentRecord>>().apply {
        value = paymentList
    }
    val payments: LiveData<List<PaymentRecord>>
        get() = _paymentList

    fun addPaymentToHistory(newPayment: PaymentRecord) {
        paymentList.add(newPayment)
        repo.add(newPayment)
    }
}