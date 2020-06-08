package com.vonHousen.kappusta.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vonHousen.kappusta.reporting.ReportRepository

class SettingsViewModel : ViewModel() {

    private val repo = ReportRepository

    private val _textMoneyBudget = MutableLiveData<String>().apply {
        value = repo.getCurrentBudget().toString()
    }
    private var moneyBudget: Double = repo.getCurrentBudget()
    val textMoneyBudget: LiveData<String> = _textMoneyBudget
    fun updateBudgetTxt(money: Double) {
        _textMoneyBudget.value = "$money"
        moneyBudget = money
        repo.setCurrentBudget(money)
    }

}