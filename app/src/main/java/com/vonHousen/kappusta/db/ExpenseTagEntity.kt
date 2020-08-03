package com.vonHousen.kappusta.db

import androidx.room.*
import java.time.LocalDate

@Entity(
    tableName = "EXPENSE_TAGS"
)
data class ExpenseTagEntity (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "expense_tag_id")
    var expenseTagID: Int?,

    @ColumnInfo(name = "expense_tag")
    var expenseTag: String,

    @ColumnInfo(name = "created_date")
    var createdDate: LocalDate

) {
    constructor(expenseTag: String) : this(
        expenseTagID = null,
        expenseTag = expenseTag,
        createdDate = LocalDate.now()
    )
}