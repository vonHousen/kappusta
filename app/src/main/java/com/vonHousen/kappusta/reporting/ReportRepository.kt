package com.vonHousen.kappusta.reporting

import com.vonHousen.kappusta.MainActivity
import com.vonHousen.kappusta.db.*
import com.vonHousen.kappusta.ui.history.AvgCurvesData
import com.vonHousen.kappusta.ui.history.SpendingCurveData
import java.time.LocalDate
import java.time.temporal.ChronoUnit

object ReportRepository {
    private val reportDAO: ReportDAO = MainActivity.db.reportDAO()
    private var expenseTypes: MutableList<ExpenseType> = getAllExpenseTypes().toMutableList()
    private var profitTypes: MutableList<ProfitType> = getAllProfitTypes().toMutableList()
    private val today: LocalDate = LocalDate.now()
    private val firstDayOfCurrentMonth: LocalDate = today.withDayOfMonth(1)
    private val lastDayOfCurrentMonth: LocalDate = today.withDayOfMonth(today.lengthOfMonth())


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
        val currentBudget : Money = getCurrentBudget()
        val moneyLeft : Money = currentBudget -
            reportDAO.howMuchMoneyIsSpentBetween(firstDayOfCurrentMonth, lastDayOfCurrentMonth)

        if (currentBudget > 0.0) {
            val fractionLeft = moneyLeft / currentBudget
            val fractionOfMonth = today.until(lastDayOfCurrentMonth, ChronoUnit.DAYS).toDouble() /
                    firstDayOfCurrentMonth.until(lastDayOfCurrentMonth, ChronoUnit.DAYS)

            return SummaryReport(
                moneyLeft,
                (100 * fractionLeft).toInt(),
                (100 * (fractionLeft - fractionOfMonth)).toInt()
            )
        } else {
            return SummaryReport(moneyLeft, 0, 0)
        }
    }

    fun getCurrentBudget(): Money {
        return reportDAO.getCurrentBudget(firstDayOfCurrentMonth) ?: Money(0.0)
    }

    fun setCurrentBudget(money: Money) {
        reportDAO.setCurrentBudget(BudgetEntity(firstDayOfCurrentMonth, money))
    }

    private fun getNextPaydayOfMonth(): Int {
        val monthAgo: LocalDate = today.minusMonths(1).withDayOfMonth(1)
        val firstDayOfNextMonth: LocalDate = today.plusMonths(1).withDayOfMonth(1)
        val lastDayOfNextMonth: LocalDate = firstDayOfNextMonth.withDayOfMonth(firstDayOfNextMonth.lengthOfMonth())
        val salaries = reportDAO.getClosestSalaries(monthAgo, lastDayOfNextMonth)

        val paydayOfMonth: Int
        if (salaries.count() == 0) {
            // default payday if not available
            paydayOfMonth = 10                                  // TODO make default payday configurable
        } else if (salaries[0] >= today) {
            // first payday after today if available
            val salariesNextMonth: MutableList<LocalDate> = mutableListOf()
            for (payday in salaries) {                          // salaries are sorted desc
                if (payday >= today)
                    salariesNextMonth.add(payday)
                else
                    break
            }
            salariesNextMonth.sort()
            paydayOfMonth = salariesNextMonth[0].dayOfMonth     // take the first salary after today
        } else {
            // there is no planned salary
            // deduce next salary by first one among these from max month ago
            val salariesPrevMonth: MutableList<LocalDate> = salaries.toMutableList()
            salariesPrevMonth.sort()
            paydayOfMonth = salariesPrevMonth[0].dayOfMonth     // take the first salary
        }

        return paydayOfMonth
    }

    fun getStatisticsReport(): StatisticsReport {
        val paydayOfMonth = getNextPaydayOfMonth()
        val daysSinceStartOfMonth = firstDayOfCurrentMonth.until(today, ChronoUnit.DAYS)
        val avgDaily =
            reportDAO.howMuchDailyMoneyIsSpentBetween(firstDayOfCurrentMonth, today) /
                daysSinceStartOfMonth
        val avgDailyAndSpecial =
            reportDAO.howMuchMoneyIsSpentBetween(firstDayOfCurrentMonth, today) /
                    daysSinceStartOfMonth
        val daysToPayday = if (today.dayOfMonth > paydayOfMonth) {
            today.until(lastDayOfCurrentMonth, ChronoUnit.DAYS).toInt() + paydayOfMonth
        } else {
            today.until(today.withDayOfMonth(paydayOfMonth), ChronoUnit.DAYS).toInt()
        }
        val moneyToPayday = Money(avgDailyAndSpecial.value * daysToPayday.toBigDecimal())

        return StatisticsReport(avgDaily, avgDailyAndSpecial, daysToPayday, moneyToPayday)
    }

    fun getSpendingCurveData(): SpendingCurveData {
        val expenses = reportDAO.getDailySpentBetween(firstDayOfCurrentMonth, today)
        val spendingCurve: MutableList<Pair<LocalDate, Money>> = mutableListOf()
        val budget: Money = getCurrentBudget()

        var availableMoney = budget
        var day = firstDayOfCurrentMonth
        for (expense in expenses) {
            // add available money on days that there were no expenses
            while (expense.DATE != day) {
                spendingCurve.add(Pair(day, availableMoney))
                day = day.plusDays(1)
            }
            availableMoney -= expense.SPENT
            spendingCurve.add(Pair(expense.DATE, availableMoney))
            day = day.plusDays(1)
        }
        while (day <= today) {
            spendingCurve.add(Pair(day, availableMoney))
            day = day.plusDays(1)
        }

        return SpendingCurveData(spendingCurve.toList(), budget)
    }

    fun getAvgCurvesData(): AvgCurvesData {

        fun processRollingAvg(
            points: List<SpentRecord>,
            startDay: LocalDate,
            endDay: LocalDate,
            range: Int
        ): List<Pair<LocalDate, Money>> {

            // create full timeline
            var day = startDay
            val pointsOnTimeline = mutableListOf<Pair<LocalDate, Money>>()
            for (point in points) {
                while (point.DATE != day) {
                    pointsOnTimeline.add(Pair(day, Money(0)))
                    day = day.plusDays(1)
                }
                pointsOnTimeline.add(Pair(point.DATE, point.SPENT))
                day = day.plusDays(1)
            }
            while (day <= endDay) {
                pointsOnTimeline.add(Pair(day, Money(0)))
                day = day.plusDays(1)
            }

            var sumInRange: Money
            val avgPoints = mutableListOf<Pair<LocalDate, Money>>()
            for ((idx, point) in pointsOnTimeline.withIndex()) {
                if (point.first < startDay.plusDays(range.toLong()))
                    continue
                sumInRange = Money(0)
                for (idx_inner in (idx-range)..idx) {
                    sumInRange += pointsOnTimeline[idx_inner].second
                }
                avgPoints.add(Pair(point.first, sumInRange/range))
            }
            return avgPoints
        }

        val range = 7               // TODO make it customizable
        val startDay = firstDayOfCurrentMonth.minusDays(range.toLong())
        val dailyPoints = reportDAO.getDailySpentBetween(
            startDay, today, ExpenseType.DAILY.ID
        )
        val specialPoints = reportDAO.getDailySpentBetween(
            startDay, today, ExpenseType.SPECIAL.ID
        )

        val dailyCurve = processRollingAvg(dailyPoints, startDay, today, range)
        val specialCurve = processRollingAvg(specialPoints, startDay, today, range)

        return AvgCurvesData(dailyCurve, specialCurve)
    }
}
