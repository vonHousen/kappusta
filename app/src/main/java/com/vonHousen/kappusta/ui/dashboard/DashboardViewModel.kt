package com.vonHousen.kappusta.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {

    // dashboard_avg_daily
    private val _avgDaily = MutableLiveData<String>().apply {
        value = "18,57 zł"  // TODO
    }
    val avgDaily: LiveData<String> = _avgDaily

    // dashboard_avg_daily_and_special
    private val _avgDailyAndSpecial = MutableLiveData<String>().apply {
        value = "24,85 zł"  // TODO
    }
    val avgDailyAndSpecial: LiveData<String> = _avgDailyAndSpecial

    // dashboard_salary_days
    private val _daysToSalary = MutableLiveData<String>().apply {
        value = "2"  // TODO
    }
    val daysToSalary: LiveData<String> = _daysToSalary

    // dashboard_salary_money_left
    private val _moneyLeftToSalary = MutableLiveData<String>().apply {
        value = "795,13 zł"  // TODO
    }
    val moneyLeftToSalary: LiveData<String> = _moneyLeftToSalary
}