package com.vonHousen.kappusta.db.authDB

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vonHousen.kappusta.db.Converters

@Database(
    entities = [
        ReportDBIdentifierEntity::class
    ],
    version = 5
)
@TypeConverters(Converters::class)
abstract class AuthDB : RoomDatabase() {
    abstract fun authDAO(): AuthDAO
}