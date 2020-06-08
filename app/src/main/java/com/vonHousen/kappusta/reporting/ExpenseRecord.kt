package com.vonHousen.kappusta.reporting

import com.vonHousen.kappusta.db.ExpenseEntity
import java.time.LocalDate

enum class ExpenseType(val ID: Int) {
    DAILY(1),
    SPECIAL(2),
    DUMMY(0);

    companion object {
        private val map = ExpenseType.values().associateBy(ExpenseType::ID)
        fun fromID(type: Int?) = if(type!=null) map[type] else DUMMY
    }

}   // TODO it should be bounded with names in string.xml

class ExpenseRecord(
    private var howMuch: Money,
    private var expenseType: ExpenseType?,
    private var date: LocalDate?
) {

    constructor(expenseEntity: ExpenseEntity): this(
        howMuch = expenseEntity.worth,
        expenseType = ExpenseType.fromID(expenseEntity.expenseTypeID),
        date = expenseEntity.date
    )

    constructor(reportRecord: ReportRecord): this(
        howMuch = -reportRecord.WORTH,
        expenseType = ExpenseType.valueOf(reportRecord.COMMENT!!),
        date = reportRecord.DATE
    )

    fun getHowMuch(): Money {
        return howMuch
    }

    fun getDate(): LocalDate {
        return date ?: LocalDate.now()
    }

    fun getExpenseType(): ExpenseType? {
        return expenseType
    }
}