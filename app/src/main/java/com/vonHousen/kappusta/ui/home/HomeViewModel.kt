package com.vonHousen.kappusta.ui.home

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

class HomeViewModel : ViewModel() {

    // text_home_money_left
    private val _textMoney = MutableLiveData<String>()
    val textMoney: LiveData<String> = _textMoney
    fun updateHowMuchMoneyIsLeft(moneyLeft: Money) {
        _textMoney.value = moneyLeft.getTxtWithCurrency()
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
        _textPercentAvg.value = "${percentAboveAvgTxt}%"
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