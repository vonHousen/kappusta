package com.vonHousen.kappusta.db

import androidx.room.*
import com.vonHousen.kappusta.reporting.Money
import com.vonHousen.kappusta.reporting.ReportRecord
import com.vonHousen.kappusta.reporting.SpentRecord
import java.time.LocalDate

@Dao
interface ReportDAO {

    @get:Query("select * from EXPENSES")
    val allExpenses: List<ExpenseEntity>?

    @Insert
    fun insertExpense(expense: ExpenseEntity): Long

    @Query("delete from EXPENSES where expense_id = :expenseID")
    fun deleteExpense(expenseID: Long)

    @get:Query("select * from EXPENSE_TYPES")
    val allExpenseTypes: List<ExpenseTypeEntity>?

    @Insert
    fun insertExpenseType(expenseType: ExpenseTypeEntity)

    @Delete
    fun deleteExpenseType(expenseType: ExpenseTypeEntity)

    @get:Query("select * from PROFITS")
    val allProfits: List<ProfitEntity>?

    @Insert
    fun insertProfit(profit: ProfitEntity): Long

    @Query("delete from PROFITS where profit_id = :profitID")
    fun deleteProfit(profitID: Long)

    @get:Query("select * from PROFIT_TYPES")
    val allProfitTypes: List<ProfitTypeEntity>?

    @Insert
    fun insertProfitType(profitType: ProfitTypeEntity)

    @Delete
    fun deleteProfitType(profitType: ProfitTypeEntity)

    @get:Query("""
        select DATE, WORTH, COMMENT, ID
        from (
            select 
                O.date                  DATE
            ,   -O.worth                WORTH
            ,   OT.expense_type         COMMENT
            ,   O.expense_id            ID
            from EXPENSES               O
            inner join EXPENSE_TYPES    OT
                on O.expense_type_id = OT.expense_type_id
                
            union
            select 
                P.date                  DATE
            ,   P.worth                 WORTH
            ,   PT.profit_type          COMMENT
            ,   P.profit_id             ID
            from PROFITS                P
            inner join PROFIT_TYPES     PT
                on P.profit_type_id = PT.profit_type_id
        )
        order by DATE desc
    """)
    val fullReport: List<ReportRecord>?

    @Query("""
        select
            sum(worth)                     MONEY_SPENT
        from EXPENSES
        where date between :startDate and :endDate
    """)
    fun howMuchMoneyIsSpentBetween(startDate: LocalDate, endDate: LocalDate): Money

    @Query("""
        select
            sum(worth)                     MONEY_SPENT
        from EXPENSES
        where date between :startDate and :endDate
            and expense_type_id = 1     -- DAILY
    """)
    fun howMuchDailyMoneyIsSpentBetween(startDate: LocalDate, endDate: LocalDate): Money

    @Query("""
        select budget_worth
        from BUDGET
        where first_day_date = :firstDayDate
    """)
    fun getCurrentBudget(firstDayDate: LocalDate): Money?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setCurrentBudget(budgetEntity: BudgetEntity)

    @Query("""
        select
            date            DATE
        ,   sum(worth)      SPENT
        from EXPENSES
        where date between :startDate and :endDate
        group by date
        order by date
    """)
    fun getDailySpentBetween(startDate: LocalDate, endDate: LocalDate): List<SpentRecord>

    @Query("""
        select
            date
        from PROFITS
        where profit_type_id = 1    -- SALARY
            and date between :startDate and :endDate
        group by date
        order by date desc
    """)
    fun getClosestSalaries(startDate: LocalDate, endDate: LocalDate): List<LocalDate>
}
