package com.vonHousen.kappusta.paymentRecord

import java.time.LocalDate

enum class Category {
    DAILY,
    SPECIAL,
    DUMMY
}   // TODO it should be bounded with names in string.xml

class PaymentRecord(private var howMuch: Double, private var category: Category) {

    private val dummyDate = LocalDate.parse("1996-01-01")

    fun getHowMuchString(): String {
        return "$howMuch zł"    // TODO formatting
    }

    fun getDateString(): String {
        return if (date != dummyDate) {
            date.toString()
        } else {
            "unknown date"
        }
    }

    fun getCategoryString(): String {
        return if (category != Category.DUMMY) {
            category.toString()
        } else {
            "unknown category"
        }
    }

    private var date: LocalDate = dummyDate

    constructor(howMuch: Double, date: LocalDate, category: Category) : this(howMuch, category) {
        this.date = date
        this.category = category
    }
}