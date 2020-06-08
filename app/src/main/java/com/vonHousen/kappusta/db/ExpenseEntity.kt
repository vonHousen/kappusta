package com.vonHousen.kappusta.db

import androidx.room.*
import com.vonHousen.kappusta.reporting.ExpenseRecord
import com.vonHousen.kappusta.reporting.Money
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
    var worth: Money,

    @ColumnInfo(name = "date")  // TODO think about indexing it
    var date: LocalDate

) {
    constructor(expenseRecord: ExpenseRecord) : this(
        expenseID = null,
        expenseTypeID = expenseRecord.getExpenseType()?.ID,
        worth = expenseRecord.getHowMuch(),
        date = expenseRecord.getDate()
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

    @TypeConverter
    fun moneyToReal(money: Money): Double {
        return money.value.toDouble()
    }

    @TypeConverter
    fun realToMoney(money: Double): Money {
        return Money(money)
    }
}