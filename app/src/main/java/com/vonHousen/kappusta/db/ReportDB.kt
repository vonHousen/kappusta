package com.vonHousen.kappusta.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [ReportEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class ReportDB : RoomDatabase() {
    abstract fun reportDAO(): ReportDAO
}