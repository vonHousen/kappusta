package com.vonHousen.kappusta.db

import androidx.room.TypeConverter
import com.vonHousen.kappusta.reporting.Money
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*


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