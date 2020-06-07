package com.vonHousen.kappusta.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    // text_home_money_left
    private val _textMoney = MutableLiveData<String>()
    val textMoney: LiveData<String> = _textMoney
    fun updateHowMuchMoneyIsLeft(moneyLeft: Double) {
        _textMoney.value = "$moneyLeft zł"
    }

    // text_home_percent_left
    private val _textPercentLeft = MutableLiveData<String>()
    val textPercentLeft: LiveData<String> = _textPercentLeft
    fun updateHowMuchPercentIsLeft(percentLeft: Int) {
        _textPercentLeft.value = "${percentLeft}%"
    }

    // text_home_percent_avg
    private val _textPercentAvg = MutableLiveData<String>()
    val textPercentAvg: LiveData<String> = _textPercentAvg
    fun updateHowMuchPercentAboveAvg(percentAboveAvg: Int) {
        _textPercentAvg.value = "(${percentAboveAvg}%)"
    }

}