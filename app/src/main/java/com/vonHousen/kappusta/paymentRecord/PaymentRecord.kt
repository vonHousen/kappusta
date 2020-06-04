package com.vonHousen.kappusta.paymentRecord

import java.time.LocalDate

enum class Category {
    DAILY,
    SPECIAL,
    DUMMY
}

class PaymentRecord(var howMuch: Double) {

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

    var date: LocalDate = dummyDate
    var category: Category = Category.DUMMY

    constructor(howMuch: Double, date: LocalDate, category: Category) : this(howMuch) {
        this.date = date
        this.category = category
    }
}