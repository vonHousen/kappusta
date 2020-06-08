package com.vonHousen.kappusta.reporting

import java.math.BigDecimal
import java.math.RoundingMode

class Money(money: BigDecimal) {
    val value : BigDecimal = money.setScale(2, RoundingMode.HALF_EVEN)

    constructor(moneyValue: Double) : this(moneyValue.toBigDecimal())
    constructor(moneyValue: String) : this(moneyValue.toBigDecimal())

    operator fun plus(other: Money) = Money(value + other.value)
    operator fun minus(other: Money) = Money(value - other.value)
    operator fun div(other: Money) = value.toDouble() / other.value.toDouble()
    operator fun unaryMinus(): Money = Money(-value)
    operator fun compareTo(other: Int): Int = value.compareTo(other.toBigDecimal())
    operator fun compareTo(other: Double): Int = value.compareTo(other.toBigDecimal())
    operator fun compareTo(other: Money): Int = value.compareTo(other.value)
    override fun toString(): String = value.toString()
}