package com.vonHousen.kappusta.db

import androidx.room.*
import com.vonHousen.kappusta.payment.PaymentRecord
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

@Entity(tableName = "PAYMENT_REPORT")
data class ReportEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "payment_id")
    var id: Int?,

    @ColumnInfo(name = "category")
    var category: String,

    @ColumnInfo(name = "how_much")
    var howMuch: Double,

    @ColumnInfo(name = "date")
    var date: LocalDate

) {
    constructor(reportRecord: PaymentRecord) : this(
        id = null,
        category = reportRecord.getCategoryString(),
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