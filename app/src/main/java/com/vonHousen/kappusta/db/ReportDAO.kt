package com.vonHousen.kappusta.db

import androidx.room.*
import com.vonHousen.kappusta.reporting.ReportRecord

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

    @get:Query("select * from PROFITS")
    val allProfits: List<ProfitEntity>?

    @Insert
    fun insertProfit(vararg profit: ProfitEntity)

    @Delete
    fun deleteProfit(vararg profit: ProfitEntity)

    @get:Query("select * from PROFIT_TYPES")
    val allProfitTypes: List<ProfitTypeEntity>?

    @Insert
    fun insertProfitType(vararg profitType: ProfitTypeEntity)

    @Delete
    fun deleteProfitType(vararg profitType: ProfitTypeEntity)

    @get:Query("""
        select DATE, WORTH, COMMENT
        from (
            select 
                O.date                  DATE
            ,   -O.worth                WORTH
            ,   OT.expense_type         COMMENT
            from EXPENSES               O
            inner join EXPENSE_TYPES    OT
                on O.expense_type_id = OT.expense_type_id
                
            union
            select 
                P.date                  DATE
            ,   P.worth                 WORTH
            ,   PT.profit_type          COMMENT
            from PROFITS                P
            inner join PROFIT_TYPES     PT
                on P.profit_type_id = PT.profit_type_id
        )
        order by DATE desc
    """)
    val fullReport: List<ReportRecord>?

}