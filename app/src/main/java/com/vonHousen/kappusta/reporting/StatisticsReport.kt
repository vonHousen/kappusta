package com.vonHousen.kappusta.reporting

data class StatisticsReport (
    var howMuchMoneySpentAvgDaily: Money,
    var howMuchMoneySpentAvgDailyAndSpecial: Money,
    var howManyDaysToPayday: Int,
    var howMuchMoneyToPayday: Money
)