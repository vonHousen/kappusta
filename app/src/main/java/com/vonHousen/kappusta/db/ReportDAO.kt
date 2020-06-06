package com.vonHousen.kappusta.db

import androidx.room.*

@Dao
interface ReportDAO {

    @get:Query("select * from EXPENSES")
    val allExpenses: List<ExpenseEntity>?

    @Insert
    fun insertReport(vararg expense: ExpenseEntity)

    @Delete
    fun deleteReport(vararg expense: ExpenseEntity)
}