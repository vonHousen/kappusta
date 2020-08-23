package com.vonHousen.kappusta.db.reportDB

import androidx.room.*
import com.vonHousen.kappusta.reporting.ExpenseType

@Entity(
    tableName = "EXPENSE_TYPES",
    indices = [Index(value = ["expense_type"], unique = true)]
)
data class ExpenseTypeEntity (

    @PrimaryKey
    @ColumnInfo(name = "expense_type_id")
    var expenseTypeID: Int,

    @ColumnInfo(name = "expense_type")
    var expenseType: String

) {
    constructor(expenseType: ExpenseType) : this(
        expenseTypeID = expenseType.ID,
        expenseType = expenseType.toString()
    )
}