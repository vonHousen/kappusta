package com.vonHousen.kappusta.db

import androidx.room.*

@Dao
interface ReportDAO {

    @get:Query("select * from PAYMENT_REPORT")
    val allReports: List<ReportEntity>?

    @Insert
    fun insertReport(vararg report: ReportEntity)

    @Delete
    fun deleteReport(vararg report: ReportEntity)
}