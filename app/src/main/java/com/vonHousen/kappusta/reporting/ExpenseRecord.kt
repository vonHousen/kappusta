package com.vonHousen.kappusta.reporting

import com.vonHousen.kappusta.db.ExpenseEntity
import java.time.LocalDate

enum class ExpenseType(val ID: Int) {
    DAILY(1),
    SPECIAL(2),
    OTHER(3),
    DUMMY(0);

    companion object {
        private val map = ExpenseType.values().associateBy(ExpenseType::ID)
        fun fromID(type: Int?) = if(type!=null) map[type] else DUMMY
    }

}   // TODO it should be bounded with names in string.xml

class ExpenseRecord(
    private var howMuch: Money,
    private var expenseType: ExpenseType?,
    private var expenseTag: String?,
    private var expenseTagID: Int?,
    private var date: LocalDate?,
    private var comment: String?,
    private var createdDate: LocalDate?,
    private var editedDate: LocalDate?
) {

    constructor(
        howMuch: Money,
        expenseType: ExpenseType,
        date: LocalDate?,
        comment: String? = null,
        expenseTagID: Int? = null,
        expenseTag: String? = null
    ): this(
        howMuch = howMuch,
        expenseType = expenseType,
        expenseTag = expenseTag,
        expenseTagID = expenseTagID,
        date = date,
        comment = comment,
        createdDate = null,
        editedDate = null
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

    fun getExpenseTagID(): Int? {
        return expenseTagID
    }

    fun getComment(): String? {
        return comment
    }

    fun getExpenseTag(): String? {
        return expenseTag
    }
}