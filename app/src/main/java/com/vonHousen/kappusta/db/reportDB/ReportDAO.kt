package com.vonHousen.kappusta.db.reportDB

import androidx.room.*
import com.vonHousen.kappusta.db.reportDB.*
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
        select DATE, WORTH, CATEGORY, COMMENT, TAG, ID
        from (
            select 
                O.date                      DATE
            ,   -O.worth                    WORTH
            ,   OT.expense_type             CATEGORY
            ,   O.comment                   COMMENT
            ,   ET.expense_tag              TAG
            ,   O.expense_id                ID
            from EXPENSES                   O
            inner join EXPENSE_TYPES        OT
                on O.expense_type_id = OT.expense_type_id
            left outer join EXPENSE_TAGS    ET
                on O.expense_tag_id = ET.expense_tag_id
                
            union
            select 
                P.date                      DATE
            ,   P.worth                     WORTH
            ,   PT.profit_type              CATEGORY
            ,   P.comment                   COMMENT
            ,   PTG.profit_tag              TAG
            ,   P.profit_id                 ID
            from PROFITS                    P
            inner join PROFIT_TYPES         PT
                on P.profit_type_id = PT.profit_type_id
            left outer join PROFIT_TAGS     PTG
                on PTG.profit_tag_id = P.profit_tag_id
        )
        order by DATE desc
    """)
    val fullReport: List<ReportRecord>?

    @Query("""
        select
            sum(worth)                     MONEY_SPENT
        from EXPENSES
        where date between :startDate and :endDate
            and expense_type_id != 3    -- OTHER
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
            and expense_type_id != 3        -- OTHER
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

    @Query("""
        select
            date            DATE
        ,   sum(worth)      SPENT
        from EXPENSES
        where date between :startDate and :endDate
            and expense_type_id = :expenseTypeID
        group by date
        order by date
    """)
    fun getDailySpentBetween(
        startDate: LocalDate,
        endDate: LocalDate,
        expenseTypeID: Int): List<SpentRecord>

    @Insert
    fun insertProfitTag(profitTag: ProfitTagEntity): Long

    @Insert
    fun insertExpenseTag(expenseTag: ExpenseTagEntity): Long

    @get:Query("select * from PROFIT_TAGS")
    val allProfitTags: List<ProfitTagEntity>?

    @get:Query("select * from EXPENSE_TAGS")
    val allExpenseTags: List<ExpenseTagEntity>?

    @Query("""
        select
            expense_tag_id
        from EXPENSES               e1
        where e1.expense_id = :expenseID
    """)
    fun getExpenseTagIDFromExpenseID(expenseID: Long): Long?

    @Query("""
        select
            profit_tag_id
        from PROFITS               p1
        where p1.profit_id = :profitID
    """)
    fun getProfitTagIDFromProfitID(profitID: Long): Long?

    @Query("""
        select
            count(expense_id)       POPULARITY_OF_EXPENSE_TAG
        from EXPENSES               e1
        where e1.expense_tag_id = :expenseTagID
    """)
    fun checkHowPopularExpenseTagIs(expenseTagID: Long): Long

    @Query("""
        select
            count(profit_id)       POPULARITY_OF_PROFIT_TAG
        from PROFITS               p1
        where p1.profit_tag_id = :profitTagID
    """)
    fun checkHowPopularProfitTagIs(profitTagID: Long): Long

    @Query("delete from EXPENSE_TAGS where expense_tag_id = :expenseTagID")
    fun deleteExpenseTag(expenseTagID: Long)

    @Query("delete from PROFIT_TAGS where profit_tag_id = :profitTagID")
    fun deleteProfitTag(profitTagID: Long)


}
