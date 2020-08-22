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
    private var expenseTagID: Int?,
    private var date: LocalDate?,
    private var comment: String?,
    private var createdDate: LocalDate?,
    private var editedDate: LocalDate?
) {

    constructor(expenseEntity: ExpenseEntity): this(
        howMuch = expenseEntity.worth,
        expenseType = ExpenseType.fromID(expenseEntity.expenseTypeID)!!,
        expenseTagID = expenseEntity.expenseTagID,
        date = expenseEntity.date,
        comment = expenseEntity.comment,
        createdDate = expenseEntity.createdDate,
        editedDate = expenseEntity.editedDate
    )

    constructor(
        howMuch: Money,
        expenseType: ExpenseType,
        date: LocalDate?,
        comment: String? = null,
        expenseTagID: Int? = null
    ): this(
        howMuch = howMuch,
        expenseType = expenseType,
        expenseTagID = expenseTagID,
        date = date,
        comment = comment,
        createdDate = null,
        editedDate = null
    )

    constructor(reportRecord: ReportRecord): this(
        howMuch = -reportRecord.WORTH,
        expenseType = ExpenseType.valueOf(reportRecord.COMMENT!!),
        expenseTagID = null,
        date = reportRecord.DATE,
        comment = null,
        createdDate = null,
        editedDate = null   // TODO check if should be null
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
}