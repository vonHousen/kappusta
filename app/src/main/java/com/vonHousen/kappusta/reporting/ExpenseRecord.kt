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
    private var howMuch: Double,
    private var expenseType: ExpenseType?,
    private var date: LocalDate?
) {

    fun getHowMuchString(): String {
        return "$howMuch zł"    // TODO formatting
    }

    fun getDateString(): String {
        return if (date != null) {
            date.toString()
        } else {
            "unknown date"
        }
    }

    fun getExpenseTypeString(): String {
        return if (expenseType != null) {
            expenseType.toString()
        } else {
            "unknown category"
        }
    }

    fun getExpenseType(): ExpenseType? {
        return expenseType
    }

    fun getExpenseTypeID(): Int? {
        return expenseType?.ID
    }

    constructor(expenseEntity: ExpenseEntity): this(
        howMuch = expenseEntity.worth,
        expenseType = ExpenseType.fromID(expenseEntity.expenseTypeID),
        date = expenseEntity.date
    )

    fun getHowMuch(): Double {
        return howMuch
    }

    fun getDate(): LocalDate {
        return date ?: LocalDate.now()
    }
}