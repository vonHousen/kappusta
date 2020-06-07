package com.vonHousen.kappusta.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _textMoney = MutableLiveData<String>().apply {
        value = "700.77 zł"
    }
    val textMoney: LiveData<String> = _textMoney
    private val _textPercentLeft = MutableLiveData<String>().apply {
        value = "77%"
    }
    val textPercentLeft: LiveData<String> = _textPercentLeft
    private val _textPercentAvg = MutableLiveData<String>().apply {
        value = "(-7%)"
    }
    val textPercentAvg: LiveData<String> = _textPercentAvg
}