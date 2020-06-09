package com.vonHousen.kappusta.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vonHousen.kappusta.reporting.Money

class DashboardViewModel : ViewModel() {

    // dashboard_avg_daily
    private val _avgDaily = MutableLiveData<String>()
    val avgDaily: LiveData<String> = _avgDaily
    fun updateAvgDaily(money: Money) {
        _avgDaily.value = money.getTxtWithCurrency()
    }

    // dashboard_avg_daily_and_special
    private val _avgDailyAndSpecial = MutableLiveData<String>()
    val avgDailyAndSpecial: LiveData<String> = _avgDailyAndSpecial
    fun updateAvgDailyAndSpecial(money: Money) {
        _avgDailyAndSpecial.value = money.getTxtWithCurrency()
    }

    // dashboard_salary_days
    private val _daysToPayday = MutableLiveData<String>()
    val daysToSalary: LiveData<String> = _daysToPayday
    fun updateDaysToPayday(days: Int) {
        _daysToPayday.value = days.toString()
    }

    // dashboard_salary_money_left
    private val _moneyLeftToPayday = MutableLiveData<String>()
    val moneyLeftToPayday: LiveData<String> = _moneyLeftToPayday
    fun updateMoneyLeftToPayday(money: Money) {
        _moneyLeftToPayday.value = money.getTxtWithCurrency()
    }
}
