package com.vonHousen.kappusta.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.vonHousen.kappusta.R
import com.vonHousen.kappusta.ui.history.HistoryViewModel
import kotlinx.android.synthetic.main.fragment_dashboard.*

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
}