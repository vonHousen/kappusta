package com.vonHousen.kappusta.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.vonHousen.kappusta.R
import com.vonHousen.kappusta.ui.history.HistoryViewModel
import kotlinx.android.synthetic.main.fragment_home.*

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

}
