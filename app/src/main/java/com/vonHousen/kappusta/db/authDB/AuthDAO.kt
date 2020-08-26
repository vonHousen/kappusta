package com.vonHousen.kappusta.db.authDB

import androidx.room.*

@Dao
interface AuthDAO {

    @Insert
    fun addReportDBName(reportDBIdentifierEntity: ReportDBIdentifierEntity): Long

    @Query("""
        select report_db_name
        from REPORT_DB_IDS
        where uid_hashed = :uidHashed
    """)
    fun getReportDBName(uidHashed: String): String?

    @Query("""
        update REPORT_DB_IDS
        set report_db_name = :reportDBName
        where id = :id
    """)
    fun setReportDBName(id: Long, reportDBName: String)

}
