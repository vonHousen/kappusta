package com.vonHousen.kappusta.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vonHousen.kappusta.reporting.Money

class HomeViewModel : ViewModel() {

    // text_home_money_left
    private val _textMoney = MutableLiveData<String>()
    val textMoney: LiveData<String> = _textMoney
    fun updateHowMuchMoneyIsLeft(moneyLeft: Money) {
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
        var percentAboveAvgTxt = percentAboveAvg.toString()
        if (percentAboveAvg > 0)
            percentAboveAvgTxt = "+$percentAboveAvgTxt"
        _textPercentAvg.value = "(${percentAboveAvgTxt}%)"
    }

}