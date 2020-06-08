package com.vonHousen.kappusta.db

import androidx.room.*
import java.time.LocalDate

@Entity(
    tableName = "BUDGET"
)
data class BudgetEntity (

    @PrimaryKey
    @ColumnInfo(name = "first_day_date")
    var firstDayDate: LocalDate,

    @ColumnInfo(name = "budget_worth")
    var budgetWorth: Double

)