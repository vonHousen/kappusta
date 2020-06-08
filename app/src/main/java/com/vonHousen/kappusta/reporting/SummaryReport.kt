package com.vonHousen.kappusta.reporting

data class SummaryReport (
    var howMuchMoneyIsLeft: Money,
    var howMuchPercentIsLeft: Int,
    var howMuchPercentAboveAvg: Int
)