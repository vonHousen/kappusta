package com.vonHousen.kappusta.ui.reporting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.vonHousen.kappusta.MainActivity
import com.vonHousen.kappusta.R
import com.vonHousen.kappusta.paymentRecord.Category
import com.vonHousen.kappusta.ui.history.HistoryViewModel
import kotlinx.android.synthetic.main.fragment_reporting.*
import java.time.LocalDate

class ReportingFragment : Fragment() {

    private lateinit var reportingViewModel: ReportingViewModel
    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var selectedDate: LocalDate
    private lateinit var selectedCategory: Category
    private lateinit var paymentValueTxt: String

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reporting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        reportingViewModel =
            ViewModelProviders.of(this).get(ReportingViewModel::class.java)
        historyViewModel =
            ViewModelProviders.of(activity!!).get(HistoryViewModel::class.java)

        button_reporting_ok.setOnClickListener {
            reportNow()
        }
        configureCategorySpinner()

        // TODO listen to "OK" signal from keyboard
    }

    private fun reportNow() {
        paymentValueTxt = reporting_payment_edit_text.text.toString()
        val reported = reportingViewModel.processNewPaymentRecord(paymentValueTxt, selectedCategory)
        if(reported != null)
            historyViewModel.addPaymentToHistory(reported)
        (activity as MainActivity).showThingsAfterReporting()
    }

    private fun configureCategorySpinner() {

        val categoriesNamesArray = resources.getStringArray(R.array.Categories)
        // Create an ArrayAdapter using a simple spinner layout and languages array
        val arrayAdapter = ArrayAdapter(
            activity!!,
            android.R.layout.simple_spinner_item,
            categoriesNamesArray
        )
        // Set layout to use when the list of choices appear
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        reporting_type_spinner!!.adapter = arrayAdapter
        reporting_type_spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                selectedCategory = reportingViewModel.categories[position]
            }
        }
    }
}
