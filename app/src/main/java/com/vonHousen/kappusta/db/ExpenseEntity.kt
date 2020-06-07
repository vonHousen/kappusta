package com.vonHousen.kappusta.db

import androidx.room.*
import com.vonHousen.kappusta.reporting.ExpenseRecord
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

@Entity(
    tableName = "EXPENSES",
    foreignKeys = [ForeignKey(
        entity = ExpenseTypeEntity::class,
        parentColumns = ["expense_type_id"],
        childColumns = ["expense_type_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ExpenseEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "expense_id")
    var expenseID: Int?,

    @ColumnInfo(name = "expense_type_id")
    var expenseTypeID: Int?,

    @ColumnInfo(name = "worth")
    var worth: Double,

    @ColumnInfo(name = "date")  // TODO think about indexing it
    var date: LocalDate

) {
    constructor(reportRecord: ExpenseRecord) : this(
        expenseID = null,
        expenseTypeID = reportRecord.getExpenseType()?.ID,
        worth = reportRecord.getHowMuch(),
        date = reportRecord.getDate()
    )
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDate? {
        val date: Date? = value?.let { Date(it) }
        return Instant.ofEpochMilli(date!!.time)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }

    @TypeConverter
    fun dateToTimestamp(localDate: LocalDate?): Long? {
        val date: Date? =
            Date.from(localDate?.atStartOfDay()?.atZone(ZoneId.systemDefault())?.toInstant())
        return date?.time?.toLong()
    }
}