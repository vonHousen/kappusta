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
            ViewModelProviders.of(requireActivity()).get(HistoryViewModel::class.java)

        configureReportingButton() // TODO listen to "OK" signal from keyboard
        configureCategorySpinner()
        configureDatePickerDialog()
        configureTags()
    }

    private fun reportNow() {
        paymentValueTxt = reporting_payment_edit_text.editText?.text.toString()
        if (selectedReportType.first != null) {     // TODO detect report type in more elegant way!
            val reportedExpense = reportingViewModel.processNewExpenseRecord(
                reportingValue = paymentValueTxt,
                expenseType = selectedReportType.first!!,
                date = selectedDate,
                expenseTagString = reporting_payment_edit_tag_txt_view.text.toString()
            )
            if (reportedExpense != null)
                historyViewModel.addExpenseToHistory(reportedExpense)
                // TODO these should inherit from the same parent, making it possible to use the same functions
        } else {
            val reportedProfit = reportingViewModel.processNewProfitRecord(
                reportingValue = paymentValueTxt,
                profitType = selectedReportType.second!!,
                date = selectedDate,
                profitTagString = reporting_payment_edit_tag_txt_view.text.toString()
            )
            if (reportedProfit != null)
                historyViewModel.addProfitToHistory(reportedProfit)
        }
        (activity as MainActivity).showThings()
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
            requireActivity(),
            android.R.layout.simple_spinner_item,
            categoriesNamesArray
        )
        // Set layout to use when the list of choices appear
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        reporting_type_spinner!!.adapter = arrayAdapter
        reporting_type_spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                selectedReportType = reportingViewModel.getReportType(position)
                configureTags()
            }
        }
        selectedReportType = reportingViewModel.getReportType(0)    // set default report type

    }

    private fun configureDatePickerDialog() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        reporting_date_button.text = selectedDate.toString()
        reporting_date_button.setOnClickListener {
            val dpd = DatePickerDialog(
                requireActivity(),
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

    private fun configureTags() {
        val items = if (selectedReportType.first != null) {     // if reporting expense
            reportingViewModel.getAllExpenseTags()
        } else {                                                // if reporting profit
            reportingViewModel.getAllProfitTags()
        }
        val adapter = ArrayAdapter(requireContext(), R.layout.tag_item, items)
        reporting_payment_edit_tag_txt_view?.setAdapter(adapter)
    }
}
