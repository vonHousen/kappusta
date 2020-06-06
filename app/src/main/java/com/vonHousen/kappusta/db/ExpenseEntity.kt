package com.vonHousen.kappusta.db

import androidx.room.*
import com.vonHousen.kappusta.reporting.ExpenseRecord
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

@Entity(tableName = "EXPENSES")
data class ExpenseEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "expense_id")
    var id: Int?,

    @ColumnInfo(name = "expense_type_id")
    var category: Int?,

    @ColumnInfo(name = "worth")
    var howMuch: Double,

    @ColumnInfo(name = "date")
    var date: LocalDate

) {
    constructor(reportRecord: ExpenseRecord) : this(
        id = null,
        category = reportRecord.getCategoryID(),
        howMuch = reportRecord.getHowMuch(),
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