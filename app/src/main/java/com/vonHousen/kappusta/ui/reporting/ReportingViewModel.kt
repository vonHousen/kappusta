package com.vonHousen.kappusta.ui.reporting

import androidx.lifecycle.ViewModel
import com.vonHousen.kappusta.db.ExpenseTagEntity
import com.vonHousen.kappusta.db.ProfitTagEntity
import com.vonHousen.kappusta.reporting.*
import java.time.LocalDate

class ReportingViewModel : ViewModel() {

    private val repo = ReportRepository

    private val categories = arrayOf(
        Pair(ExpenseType.DAILY, null),
        Pair(ExpenseType.SPECIAL, null),
        Pair(ExpenseType.OTHER, null),
        Pair(null, ProfitType.SALARY),
        Pair(null, ProfitType.BONUS),
        Pair(null, ProfitType.ONE_TIME)
    )   // TODO bind it with strings.xml

    private lateinit var profitTags: MutableList<ProfitTagEntity>
    private lateinit var expenseTags: MutableList<ExpenseTagEntity>


    fun processNewExpenseRecord(
        reportingValue: String,
        expenseType: ExpenseType,
        date: LocalDate,
        expenseTagString: String,
        comment: String
    ): ExpenseRecord? {

        if(reportingValue == "")
            return null

        var expenseTagID: Int? = null
        if (expenseTagString != "") {
            for (expenseTag in expenseTags) {
                if (expenseTagString == expenseTag.expenseTag) {
                    expenseTagID = expenseTag.expenseTagID
                    break
                }
            }
            if (expenseTagID == null) {
                expenseTagID =
                    repo.addExpenseTag(ExpenseTagEntity(expenseTag = expenseTagString)).toInt()
            }
        }

        return ExpenseRecord(
                howMuch = Money(reportingValue),
                expenseType = expenseType,
                expenseTag = if (expenseTagString != "") expenseTagString else null,
                expenseTagID = expenseTagID,
                date = date,
                comment = if (comment != "") comment else null
            )
    }

    fun processNewProfitRecord(
        reportingValue: String,
        profitType: ProfitType,
        date: LocalDate,
        profitTagString: String,
        comment: String
    ): ProfitRecord? {

        if(reportingValue == "")
            return null

        var profitTagID: Int? = null
        if (profitTagString != "") {
            for (profitTag in profitTags) {
                if (profitTagString == profitTag.profitTag) {
                    profitTagID = profitTag.profitTagID
                    break
                }
            }
            if (profitTagID == null) {
                profitTagID =
                    repo.addProfitTag(ProfitTagEntity(profitTag = profitTagString)).toInt()
            }
        }

        return ProfitRecord(
                worth = Money(reportingValue),
                profitType = profitType,
                profitTag = if (profitTagString != "") profitTagString else null,
                profitTagID = profitTagID,
                date = date,
                comment = if (comment != "") comment else null
            )
    }

    fun getReportType(position: Int): Pair<ExpenseType?, ProfitType?> = categories[position]

    fun getAllExpenseTags(): List<String> {
        val savedExpenseTags = repo.getAllExpenseTags()
        if (savedExpenseTags != null) {
            expenseTags = savedExpenseTags.toMutableList()
        } else {
            expenseTags.clear()
        }
        val expenseTagsStrings = mutableListOf<String>()
        for (expenseTag in expenseTags) {
            expenseTagsStrings.add(expenseTag.expenseTag)
        }
        return expenseTagsStrings
    }

    fun getAllProfitTags(): List<String> {
        val savedProfitTags = repo.getAllProfitTags()
        if (savedProfitTags != null) {
            profitTags = savedProfitTags.toMutableList()
        } else {
            profitTags.clear()
        }
        val profitTagsStrings = mutableListOf<String>()
        for (profitTag in profitTags) {
            profitTagsStrings.add(profitTag.profitTag)
        }
        return profitTagsStrings
    }
}