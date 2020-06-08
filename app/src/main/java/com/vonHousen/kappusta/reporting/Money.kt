package com.vonHousen.kappusta.reporting

import java.math.BigDecimal
import java.math.RoundingMode

class Money(money: BigDecimal) {
    val value : BigDecimal = money.setScale(2, RoundingMode.HALF_EVEN)
    private val currency: String = "zł"     // TODO bound it with string.xml

    fun getTxtWithCurrency() : String {
        return "$value $currency"
    }

    constructor(moneyValue: Double) : this(moneyValue.toBigDecimal())
    constructor(moneyValue: Int) : this(moneyValue.toBigDecimal())
    constructor(moneyValue: Long) : this(moneyValue.toBigDecimal())
    constructor(moneyValue: String) : this(
        if (moneyValue != "")
            moneyValue.toBigDecimal()
        else
            0.0.toBigDecimal()
    )

    operator fun plus(other: Money) = Money(value + other.value)
    operator fun minus(other: Money) = Money(value - other.value)
    operator fun div(other: Money) = value.toDouble() / other.value.toDouble()
    operator fun div(other: Long) = Money(value.toDouble() / other.toDouble())
    operator fun unaryMinus(): Money = Money(-value)
    operator fun compareTo(other: Int): Int = value.compareTo(other.toBigDecimal())
    operator fun compareTo(other: Double): Int = value.compareTo(other.toBigDecimal())
    operator fun compareTo(other: Money): Int = value.compareTo(other.value)
    override fun toString(): String = value.toString()
}