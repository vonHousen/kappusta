package com.vonHousen.kappusta.ui.home

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
import kotlinx.android.synthetic.main.fragment_home.*
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var historyViewModel: HistoryViewModel


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        historyViewModel =
            ViewModelProviders.of(activity!!).get(HistoryViewModel::class.java)

        configureTextBoxes()
        configureGraph()
    }

    private fun configureTextBoxes() {
        homeViewModel.textMoney.observe(viewLifecycleOwner, Observer {
            text_home_money_left.text = it
        })
        homeViewModel.textPercentLeft.observe(viewLifecycleOwner, Observer {
            text_home_percent_left.text = it
        })
        homeViewModel.textPercentAvg.observe(viewLifecycleOwner, Observer {
            text_home_percent_avg.text = it
        })
        historyViewModel.summaryReport.observe(viewLifecycleOwner, Observer {
            homeViewModel.updateHowMuchMoneyIsLeft(it.howMuchMoneyIsLeft)
            homeViewModel.updateHowMuchPercentIsLeft(it.howMuchPercentIsLeft)
            homeViewModel.updateHowMuchPercentAboveAvg(it.howMuchPercentAboveAvg)
        })
    }

    private fun configureGraph() {
        historyViewModel.spendingCurve.observe(viewLifecycleOwner, Observer {
            drawGraph(homeViewModel.getGraphPrerequisites(it))
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
        graph.gridLabelRenderer.numHorizontalLabels = 5
        graph.gridLabelRenderer.setHumanRounding(false)
        graph.gridLabelRenderer.textSize = 27F
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
