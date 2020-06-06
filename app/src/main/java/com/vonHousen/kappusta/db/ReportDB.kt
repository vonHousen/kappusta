package com.vonHousen.kappusta.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        ExpenseEntity::class,
        ExpenseTypeEntity::class,
        ProfitEntity::class,
        ProfitTypeEntity::class
    ],
    version = 4
)
@TypeConverters(Converters::class)
abstract class ReportDB : RoomDatabase() {
    abstract fun reportDAO(): ReportDAO
}