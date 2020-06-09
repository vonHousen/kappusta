package com.vonHousen.kappusta.reporting

import java.time.LocalDate

data class SpentRecord (
    val DATE: LocalDate,
    val SPENT: Money
)