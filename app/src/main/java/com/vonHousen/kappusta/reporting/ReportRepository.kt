package com.vonHousen.kappusta.reporting

import com.vonHousen.kappusta.MainActivity
import com.vonHousen.kappusta.db.*
import java.time.LocalDate
import java.time.temporal.ChronoUnit

object ReportRepository {
    private val reportDAO: ReportDAO = MainActivity.db.reportDAO()
    private var expenseTypes: MutableList<ExpenseType> = getAllExpenseTypes().toMutableList()
    private var profitTypes: MutableList<ProfitType> = getAllProfitTypes().toMutableList()

    fun addExpense(expenseRecord: ExpenseRecord): Long {
        val expenseType = expenseRecord.getExpenseType()
        if(!expenseTypes.contains(expenseType) && expenseType != null) {
            expenseTypes.add(expenseType)
            reportDAO.insertExpenseType(ExpenseTypeEntity(expenseType))
        }
        return reportDAO.insertExpense(ExpenseEntity(expenseRecord))
    }

    fun addProfit(profitRecord: ProfitRecord): Long {
        val profitType = profitRecord.getProfitType()
        if(!profitTypes.contains(profitType) && profitType != null) {
            profitTypes.add(profitType)
            reportDAO.insertProfitType(ProfitTypeEntity(profitType))
        }
        return reportDAO.insertProfit(ProfitEntity(profitRecord))
    }

    fun getAllExpenses(): List<ExpenseRecord> {
        val expensesTmp = mutableListOf<ExpenseRecord>()
        val loadedExpenses = reportDAO.allExpenses
        if (loadedExpenses != null) {
            for (expense in loadedExpenses) {
                expensesTmp.add(ExpenseRecord(expense))
            }
        }
        return expensesTmp
    }

    fun getAllExpenseTypes(): List<ExpenseType> {
        val expensesTypesTmp = mutableListOf<ExpenseType>()
        val loadedExpenseTypes = reportDAO.allExpenseTypes
        if (loadedExpenseTypes != null) {
            for (expenseType in loadedExpenseTypes) {
                expensesTypesTmp.add(ExpenseType.fromID(expenseType.expenseTypeID)!!)
            }
        }
        return expensesTypesTmp
    }

    fun getAllProfitTypes(): List<ProfitType> {
        val profitTypesTmp = mutableListOf<ProfitType>()
        val loadedProfitTypes = reportDAO.allProfitTypes
        if (loadedProfitTypes != null) {
            for (profitType in loadedProfitTypes) {
                profitTypesTmp.add(ProfitType.fromID(profitType.profitTypeID)!!)
            }
        }
        return profitTypesTmp
    }

    fun getFullReport(): List<ReportRecord> {
        return reportDAO.fullReport ?: listOf<ReportRecord>()
    }

    fun removeReport(reportRecord: ReportRecord) {
        if (reportRecord.WORTH < 0)
            reportDAO.deleteExpense(reportRecord.ID)
        else
            reportDAO.deleteProfit(reportRecord.ID)
    }

    fun getSummaryReport(): SummaryReport {
        val today: LocalDate = LocalDate.now()
        val firstDayOfCurrentMonth: LocalDate = today.withDayOfMonth(1)
        val lastDayOfCurrentMonth: LocalDate = today.withDayOfMonth(today.lengthOfMonth())
        val leftMoneyTuple =
            reportDAO.howMuchMoneyIsLeft(firstDayOfCurrentMonth, lastDayOfCurrentMonth)
        val moneyLeft = leftMoneyTuple.MONEY_LEFT
        val fractionLeft = leftMoneyTuple.FRACTION_LEFT
        val fractionOfMonth = today.until(lastDayOfCurrentMonth, ChronoUnit.DAYS).toDouble() /
                firstDayOfCurrentMonth.until(lastDayOfCurrentMonth, ChronoUnit.DAYS)

        return SummaryReport(
            moneyLeft,
            (100 * fractionLeft).toInt(),
            (100 * (fractionLeft - fractionOfMonth)).toInt()
        )
    }

    fun getCurrentBudget(): Double {
        return 1000.0
    }

    fun setCurrentBudget(money: Double) {
    }
}