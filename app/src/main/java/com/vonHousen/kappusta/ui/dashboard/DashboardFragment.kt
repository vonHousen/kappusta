package com.vonHousen.kappusta.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.LegendRenderer
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
        historyViewModel.avgCurves.observe(viewLifecycleOwner, Observer {
            drawGraph(dashboardViewModel.getGraphPrerequisites(it))
        })

        val graph: GraphView = avg_curve_graph
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
        graph.gridLabelRenderer.numHorizontalLabels = 5
        graph.gridLabelRenderer.setHumanRounding(false)
        graph.gridLabelRenderer.textSize = 27F
        graph.legendRenderer.isVisible = true
        graph.legendRenderer.align = LegendRenderer.LegendAlign.TOP
        graph.legendRenderer.backgroundColor = resources.getColor(R.color.colorDarkBackground)
        graph.legendRenderer.textSize = 32F

    }

    private fun drawGraph(prerequisites: AvgGraphPrerequisites) {
        val graph: GraphView = avg_curve_graph
        graph.removeAllSeries()

        // daily data series
        val dailySeries = prerequisites.dataSeriesDaily
        graph.addSeries(dailySeries)
        dailySeries.color = resources.getColor(R.color.colorAccent)
        dailySeries.isDrawBackground = true
        dailySeries.backgroundColor = resources.getColor(R.color.colorAccentSemiTransparent)
        dailySeries.title = resources.getString(R.string.data_series_daily_avg_title)

        // special data series
        val specialSeries = prerequisites.dataSeriesSpecial
        graph.addSeries(specialSeries)
        specialSeries.color = resources.getColor(R.color.colorPrimary)
        specialSeries.title = resources.getString(R.string.data_series_special_avg_title)

        // graph configuration
        graph.viewport.setMinX(prerequisites.firstDay.time.toDouble())
        graph.viewport.setMaxX(prerequisites.lastDay.time.toDouble())
    }

}