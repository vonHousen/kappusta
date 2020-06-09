package com.vonHousen.kappusta.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.vonHousen.kappusta.reporting.Money
import com.vonHousen.kappusta.ui.history.AvgCurvesData
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


    fun getGraphPrerequisites(avgCurvesData: AvgCurvesData): AvgGraphPrerequisites {
        fun transformIntoDataSeries(curve: List<Pair<LocalDate, Money>>): LineGraphSeries<DataPoint> {
            val dailyDataSeries: LineGraphSeries<DataPoint> = LineGraphSeries()
            val pointCount = curve.count()
            for (point in curve) {
                val day: Date = Date.from(
                    point.first.atStartOfDay()?.atZone(ZoneId.systemDefault())?.toInstant()
                )
                val value: Double = point.second.value.toDouble()
                dailyDataSeries.appendData(DataPoint(day, value), false, pointCount)
            }
            return dailyDataSeries
        }

        val dailyCurve = avgCurvesData.avgDailyCurve
        val specialCurve = avgCurvesData.avgSpecialCurve

        val today: LocalDate = LocalDate.now()
        val firstDayOfCurrentMonth: Date = Date.from(
            today.withDayOfMonth(1)
                .atStartOfDay()?.atZone(ZoneId.systemDefault())?.toInstant()
        )
        val lastDayOfCurrentMonth: Date = Date.from(
            today.withDayOfMonth(today.lengthOfMonth())
                .atStartOfDay()?.atZone(ZoneId.systemDefault())?.toInstant()
        )

        return AvgGraphPrerequisites(
            transformIntoDataSeries(dailyCurve),
            transformIntoDataSeries(specialCurve),
            firstDayOfCurrentMonth,
            lastDayOfCurrentMonth
        )
    }
}

data class AvgGraphPrerequisites (
    val dataSeriesDaily: LineGraphSeries<DataPoint>,
    val dataSeriesSpecial: LineGraphSeries<DataPoint>,
    val firstDay: Date,
    val lastDay: Date
)