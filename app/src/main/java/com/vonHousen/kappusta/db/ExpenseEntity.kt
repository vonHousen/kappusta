package com.vonHousen.kappusta.db

import androidx.annotation.NonNull
import androidx.room.*
import com.vonHousen.kappusta.reporting.ExpenseRecord
import com.vonHousen.kappusta.reporting.Money
import java.time.LocalDate


@Entity(
    tableName = "EXPENSES",
    foreignKeys = [
        ForeignKey(
            entity = ExpenseTypeEntity::class,
            parentColumns = ["expense_type_id"],
            childColumns = ["expense_type_id"],
            onDelete = ForeignKey.CASCADE),
        ForeignKey(
            entity = ExpenseTagEntity::class,
            parentColumns = ["expense_tag_id"],
            childColumns = ["expense_tag_id"],
            onDelete = ForeignKey.CASCADE)
    ]
)
data class ExpenseEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "expense_id")
    var expenseID: Int?,

    @ColumnInfo(name = "expense_type_id", index = true)
    var expenseTypeID: Int,

    @ColumnInfo(name = "expense_tag_id", index = true)
    var expenseTagID: Int?,

    @ColumnInfo(name = "worth")
    var worth: Money,

    @ColumnInfo(name = "date")  // TODO think about indexing it
    var date: LocalDate,

    @ColumnInfo(name = "comment")
    var comment: String?,

    @ColumnInfo(name = "created_date")
    var createdDate: LocalDate,

    @ColumnInfo(name = "edited_date")
    var editedDate: LocalDate

) {
    constructor(expenseRecord: ExpenseRecord) : this(
        expenseID = null,
        expenseTypeID = expenseRecord.getExpenseType()!!.ID,               // TODO check that out
        expenseTagID = expenseRecord.getExpenseTagID(),
        worth = expenseRecord.getHowMuch(),
        date = expenseRecord.getDate(),
        comment = expenseRecord.getComment(),
        createdDate = LocalDate.now(),
        editedDate = LocalDate.now()
    )
}
