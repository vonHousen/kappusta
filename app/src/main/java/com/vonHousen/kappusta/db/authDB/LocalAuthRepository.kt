package com.vonHousen.kappusta.db.authDB

import android.content.Context
import com.vonHousen.kappusta.MainActivity
import java.io.File

class LocalAuthRepository (private val parentContext: Context) {
    private val authDAO: AuthDAO = MainActivity.auth_db.authDAO()

    fun getDBNameForUser(uidHashed: String): String {
        var dbName: String? = authDAO.getReportDBName(uidHashed)
        if (dbName == null) {
            dbName = checkForLegacyDatabase(uidHashed)
            if (dbName == null) {
                dbName = addNewReportDBName(uidHashed)
            }
        }
        return dbName
    }

    private fun checkForLegacyDatabase(uidHashed: String): String? {
        val legacyDBName = "ReportDB"
        val pathToLegacyDBName = "databases/${legacyDBName}"
        val file = File(parentContext.dataDir, pathToLegacyDBName)
        if(!file.exists())
            return null
        authDAO.addReportDBName(ReportDBIdentifierEntity(uidHashed, legacyDBName))
        return legacyDBName
    }

    private fun addNewReportDBName(uidHashed: String): String {
        val newID = authDAO.addReportDBName(ReportDBIdentifierEntity(uidHashed))
        val newDBName = "ReportDB-${newID}"
        authDAO.setReportDBName(newID, newDBName)
        return newDBName
    }
}
