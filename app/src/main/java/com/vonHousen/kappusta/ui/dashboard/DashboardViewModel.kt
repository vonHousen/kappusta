package com.vonHousen.kappusta.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.vonHousen.kappusta.reporting.Money
import com.vonHousen.kappusta.ui.history.SpendingCurveData
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

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

    fun getGraphPrerequisites(spendingCurveData: SpendingCurveData): GraphPrerequisites {
        val budget = spendingCurveData.budget
        val series = spendingCurveData.spendingCurve

        val today: LocalDate = LocalDate.now()
        val firstDayOfCurrentMonth: Date = Date.from(
            today.withDayOfMonth(1)
                .atStartOfDay()?.atZone(ZoneId.systemDefault())?.toInstant()
        )
        val lastDayOfCurrentMonth: Date = Date.from(
            today.withDayOfMonth(today.lengthOfMonth())
                .atStartOfDay()?.atZone(ZoneId.systemDefault())?.toInstant()
        )
        val dataSeriesPredict: LineGraphSeries<DataPoint> = LineGraphSeries(arrayOf(
            DataPoint(firstDayOfCurrentMonth, budget.value.toDouble()),
            DataPoint(lastDayOfCurrentMonth, 0.0)
        ))

        val dataSeriesSpending: LineGraphSeries<DataPoint> = LineGraphSeries()
        if (series != null) {
            val pointCount = series.count()
            for (point in series) {
                val day: Date = Date.from(
                    point.first.atStartOfDay()?.atZone(ZoneId.systemDefault())?.toInstant()
                )
                val value: Double = point.second.value.toDouble()
                dataSeriesSpending.appendData(DataPoint(day, value), false, pointCount)
            }
        }
        return GraphPrerequisites(
            dataSeriesSpending, dataSeriesPredict, firstDayOfCurrentMonth, lastDayOfCurrentMonth
        )
    }
}

data class GraphPrerequisites (
    val dataSeriesSpending: LineGraphSeries<DataPoint>,
    val dataSeriesPredict: LineGraphSeries<DataPoint>,
    val firstDay: Date,
    val lastDay: Date
)