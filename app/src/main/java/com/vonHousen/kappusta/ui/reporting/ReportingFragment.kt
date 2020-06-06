package com.vonHousen.kappusta.ui.reporting

import android.app.DatePickerDialog
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
import com.vonHousen.kappusta.reporting.ExpenseType
import com.vonHousen.kappusta.reporting.ProfitType
import com.vonHousen.kappusta.ui.history.HistoryViewModel
import kotlinx.android.synthetic.main.fragment_reporting.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*


class ReportingFragment : Fragment() {

    private lateinit var reportingViewModel: ReportingViewModel
    private lateinit var historyViewModel: HistoryViewModel
    private var selectedDate: LocalDate = LocalDate.now()
    private lateinit var selectedReportType: Pair<ExpenseType?, ProfitType?>
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

        configureReportingButton() // TODO listen to "OK" signal from keyboard
        configureCategorySpinner()
        configureDatePickerDialog()
    }

    private fun reportNow() {
        paymentValueTxt = reporting_payment_edit_text.editText?.text.toString()
        if (selectedReportType.first != null) {
            val reportedExpense = reportingViewModel.processNewExpenseRecord(
                paymentValueTxt,
                selectedReportType.first!!,
                selectedDate
            )
            if (reportedExpense != null)
                historyViewModel.addExpenseToHistory(reportedExpense)
        } else {
            val reportedProfit = reportingViewModel.processNewProfitRecord(
                paymentValueTxt,
                selectedReportType.second!!,
                selectedDate
            )
            if (reportedProfit != null)
                historyViewModel.addProfitToHistory(reportedProfit)
        }
        (activity as MainActivity).showThingsAfterReporting()
    }

    private fun configureReportingButton() {
        button_reporting_ok.setOnClickListener {
            reportNow()
        }
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
                selectedReportType = reportingViewModel.getReportType(position)
            }
        }
    }

    private fun configureDatePickerDialog() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        reporting_date_button.text = selectedDate.toString()
        reporting_date_button.setOnClickListener {
            val dpd = DatePickerDialog(
                activity!!,
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    val day1: Int = dayOfMonth
                    val month1: Int = monthOfYear
                    val year1: Int = year
                    val calendar = Calendar.getInstance()
                    calendar[year1, month1] = day1

                    val sdf: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
                    val formatedDate: String = sdf.format(calendar.time)
                    selectedDate = sdf.parse(formatedDate)!!.toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate()
                    reporting_date_button.text = selectedDate.toString()

                }, year, month, day)
            dpd.show()
        }
    }
}
