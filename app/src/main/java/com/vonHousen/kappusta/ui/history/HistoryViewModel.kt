package com.vonHousen.kappusta.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vonHousen.kappusta.reporting.*
import java.time.LocalDate

class HistoryViewModel : ViewModel() {

    private val repo = ReportRepository

    private var reportList = repo.getFullReport().toMutableList()
    private val _reportList = MutableLiveData<List<ReportRecord>>().apply {
        value = reportList
    }
    val reports: LiveData<List<ReportRecord>>
        get() = _reportList

    private val _summaryReport = MutableLiveData<SummaryReport>().apply {
        value = repo.getSummaryReport()
    }
    val summaryReport: LiveData<SummaryReport>
        get() = _summaryReport

    private val _statisticsReport = MutableLiveData<StatisticsReport>().apply {
        value = repo.getStatisticsReport()
    }
    val statisticsReport: LiveData<StatisticsReport>
        get() = _statisticsReport

    private val _moneyBudget = MutableLiveData<Money>().apply {
        value = repo.getCurrentBudget()
    }
    val moneyBudget: LiveData<Money>
        get() = _moneyBudget

    private val _spendingCurve = MutableLiveData<SpendingCurveData>().apply {
        value = repo.getSpendingCurveData()
    }
    val spendingCurve: LiveData<SpendingCurveData>
        get() = _spendingCurve

    private val _avgCurves = MutableLiveData<AvgCurvesData>().apply {
        value = repo.getAvgCurvesData()
    }
    val avgCurves: LiveData<AvgCurvesData>
        get() = _avgCurves

    fun addExpenseToHistory(newExpense: ExpenseRecord) {
        val addedID = repo.addExpense(newExpense)
        val newReport = ReportRecord(
            DATE = newExpense.getDate(),
            WORTH = -newExpense.getHowMuch(),
            CATEGORY = newExpense.getExpenseType().toString(),
            COMMENT = newExpense.getComment(),
            TAG = newExpense.getExpenseTag(),
            ID = addedID
        )
        reportList.add(newReport)
        reportList.sortByDescending { it.DATE }
        updateSummaryReport()
        updateStatisticsReport()
        updateSpendingCurve()
        updateAvgCurves()
    }

    fun addProfitToHistory(newProfit: ProfitRecord) {
        val addedID = repo.addProfit(newProfit)
        val newReport = ReportRecord(
            DATE = newProfit.getDate(),
            WORTH = newProfit.getHowMuch(),
            CATEGORY = newProfit.getProfitType().toString(),
            COMMENT = newProfit.getComment(),
            TAG = newProfit.getProfitTag(),
            ID = addedID
        )
        reportList.add(newReport)
        reportList.sortByDescending { it.DATE }
        updateSummaryReport()
        updateStatisticsReport()
    }

    fun notifyReportRemoved(position: Int) {
        val removedItem = reportList.removeAt(position)
        repo.removeReport(removedItem)
        updateSummaryReport()
        updateStatisticsReport()
        updateSpendingCurve()
        updateAvgCurves()
    }

    private fun updateSummaryReport() {
        _summaryReport.value = repo.getSummaryReport()          // TODO you don't need to query db again
    }

    private fun updateStatisticsReport() {
        _statisticsReport.value = repo.getStatisticsReport()    // TODO you don't need to query db again
    }

    fun updateBudget(newBudget: Money) {
        _moneyBudget.value = newBudget
        repo.setCurrentBudget(newBudget)
        updateSummaryReport()
        updateSpendingCurve()
    }

    private fun updateSpendingCurve() {
        _spendingCurve.value = repo.getSpendingCurveData()      // TODO you don't need to query db again
    }

    private fun getRealCategoryFromString(categoryTxt: String?, realCategoryNames: Array<String>): String? {
        val categoryNames = arrayOf(
            ExpenseType.DAILY.toString(),
            ExpenseType.SPECIAL.toString(),
            ExpenseType.OTHER.toString(),
            ProfitType.SALARY.toString(),
            ProfitType.BONUS.toString(),
            ProfitType.ONE_TIME.toString()
        )   // TODO bind it with strings.xml
        if (categoryNames.size != realCategoryNames.size)
            return null
        for ((idx, name) in categoryNames.withIndex()) {
            if (categoryTxt == name)
                return realCategoryNames[idx]
        }
        return null
    }

    fun prepareReport(
        reportingHistoryList: List<ReportRecord>,
        categoryNames: Array<String>
    ): MutableList<ReportRecord> {
        val report = reportingHistoryList.map{ it.copy() }.toMutableList()
        for (reportRecord in report) {
            reportRecord.CATEGORY = getRealCategoryFromString(reportRecord.CATEGORY, categoryNames)
        }
        return report
    }

    private fun updateAvgCurves() {
        _avgCurves.value = repo.getAvgCurvesData()          // TODO you don't need to query db again
    }
}

data class SpendingCurveData (
    val spendingCurve: List<Pair<LocalDate, Money>>,
    val budget: Money
)

data class AvgCurvesData (
    val avgDailyCurve: List<Pair<LocalDate, Money>>,
    val avgSpecialCurve: List<Pair<LocalDate, Money>>
)