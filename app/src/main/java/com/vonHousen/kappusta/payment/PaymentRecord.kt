package com.vonHousen.kappusta.payment

import com.vonHousen.kappusta.db.ReportEntity
import java.time.LocalDate

enum class Category {
    DAILY,
    SPECIAL,
    DUMMY
}   // TODO it should be bounded with names in string.xml

class PaymentRecord(
    private var howMuch: Double,
    private var category: Category?,
    private var date: LocalDate?
) {

    fun getHowMuchString(): String {
        return "$howMuch zł"    // TODO formatting
    }

    fun getDateString(): String {
        return if (date != null) {
            date.toString()
        } else {
            "unknown date"
        }
    }

    fun getCategoryString(): String {
        return if (category != null) {
            category.toString()
        } else {
            "unknown category"
        }
    }

    constructor(reportEntity: ReportEntity): this(
        howMuch = reportEntity.howMuch,
        category = Category.valueOf(reportEntity.category),
        date = reportEntity.date
    )

    fun getHowMuch(): Double {
        return howMuch
    }

    fun getDate(): LocalDate {
        return date ?: LocalDate.now()
    }
}