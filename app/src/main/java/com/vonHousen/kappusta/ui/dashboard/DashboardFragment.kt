package com.vonHousen.kappusta.ui.dashboard

import android.graphics.DashPathEffect
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.GraphView
import com.vonHousen.kappusta.R
import com.vonHousen.kappusta.ui.history.HistoryViewModel
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var historyViewModel: HistoryViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        historyViewModel =
            ViewModelProviders.of(activity!!).get(HistoryViewModel::class.java)

        configureTxtAvg()
        configureTxtSalary()
        configureGraph()
    }

    private fun configureTxtAvg() {
        dashboardViewModel.avgDaily.observe(viewLifecycleOwner, Observer {
            dashboard_avg_daily.text = it
        })
        dashboardViewModel.avgDailyAndSpecial.observe(viewLifecycleOwner, Observer {
            dashboard_avg_daily_and_special.text = it
        })
        historyViewModel.statisticsReport.observe(viewLifecycleOwner, Observer {
            dashboardViewModel.updateAvgDaily(it.howMuchMoneySpentAvgDaily)
            dashboardViewModel.updateAvgDailyAndSpecial(it.howMuchMoneySpentAvgDailyAndSpecial)
        })
    }

    private fun configureTxtSalary() {
        dashboardViewModel.daysToSalary.observe(viewLifecycleOwner, Observer {
            dashboard_salary_days.text = it
        })
        dashboardViewModel.moneyLeftToPayday.observe(viewLifecycleOwner, Observer {
            dashboard_salary_money_left.text = it
        })
        historyViewModel.statisticsReport.observe(viewLifecycleOwner, Observer {
            dashboardViewModel.updateDaysToPayday(it.howManyDaysToPayday)
            dashboardViewModel.updateMoneyLeftToPayday(it.howMuchMoneyToPayday)
        })
    }

    private fun configureGraph() {
        historyViewModel.spendingCurve.observe(viewLifecycleOwner, Observer {
            drawGraph(dashboardViewModel.getGraphPrerequisites(it))
        })

        val graph: GraphView = dashboard_graph
        // custom label formatter
        graph.gridLabelRenderer.labelFormatter = object : DefaultLabelFormatter() {
            override fun formatLabel(value: Double, isValueX: Boolean): String {
                return if (isValueX) {
                    // show date for x values
                    val date = Date(value.toLong())
                    val localDate = Instant.ofEpochMilli(date.time)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                    return localDate.format(DateTimeFormatter.ofPattern("dd.MM"))
                } else {
                    // show currency for y values
                    super.formatLabel(value, isValueX) +
                            " ${resources.getString(R.string.currency_txt)}"
                }
            }
        }
        graph.viewport.isXAxisBoundsManual = true
        graph.gridLabelRenderer.numHorizontalLabels = 4
        graph.gridLabelRenderer.setHumanRounding(false)
//        graph.legendRenderer.isVisible = true
//        graph.legendRenderer.align = LegendRenderer.LegendAlign.TOP
//        graph.legendRenderer.backgroundColor = resources.getColor(R.color.colorDarkBackground)
//        graph.legendRenderer.textSize = 30F
    }

    private fun drawGraph(prerequisites: GraphPrerequisites) {
        val graph: GraphView = dashboard_graph
        graph.removeAllSeries()

        // spending data series
        val dataSeriesSpending = prerequisites.dataSeriesSpending
        graph.addSeries(dataSeriesSpending)
        dataSeriesSpending.color = resources.getColor(R.color.colorAccent)
        dataSeriesSpending.isDrawBackground = true
        dataSeriesSpending.backgroundColor = resources.getColor(R.color.colorAccentSemiTransparent)
        dataSeriesSpending.title = resources.getString(R.string.data_series_spending_title)

        // predicted data series
        val dataSeriesPredicted = prerequisites.dataSeriesPredict
        graph.addSeries(dataSeriesPredicted)
        // custom paint to make a dotted line
        val paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5F
        paint.pathEffect = DashPathEffect(floatArrayOf(50f, 30f), 1F)
        paint.color = resources.getColor(R.color.colorPrimary)
        dataSeriesPredicted.setCustomPaint(paint)
        dataSeriesPredicted.title = resources.getString(R.string.data_series_predicted_title)

        // graph configuration
        graph.viewport.setMinX(prerequisites.firstDay.time.toDouble())
        graph.viewport.setMaxX(prerequisites.lastDay.time.toDouble())
    }
}