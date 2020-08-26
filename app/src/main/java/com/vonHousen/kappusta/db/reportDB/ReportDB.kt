package com.vonHousen.kappusta.db.reportDB

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vonHousen.kappusta.db.Converters

@Database(
    entities = [
        ExpenseEntity::class,
        ExpenseTypeEntity::class,
        ExpenseTagEntity::class,
        ProfitEntity::class,
        ProfitTypeEntity::class,
        ProfitTagEntity::class,
        BudgetEntity::class
    ],
    version = 7
)
@TypeConverters(Converters::class)
abstract class ReportDB : RoomDatabase() {
    abstract fun reportDAO(): ReportDAO
}