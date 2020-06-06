package com.vonHousen.kappusta.db

import androidx.room.*

@Dao
interface ReportDAO {

    @get:Query("select * from EXPENSES")
    val allExpenses: List<ExpenseEntity>?

    @Insert
    fun insertExpense(vararg expense: ExpenseEntity)

    @Delete
    fun deleteExpense(vararg expense: ExpenseEntity)

    @get:Query("select * from EXPENSE_TYPES")
    val allExpenseTypes: List<ExpenseTypeEntity>?

    @Insert
    fun insertExpenseType(vararg expenseType: ExpenseTypeEntity)

    @Delete
    fun deleteExpenseType(vararg expenseType: ExpenseTypeEntity)
}