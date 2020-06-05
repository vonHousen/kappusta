package com.vonHousen.kappusta.payment

import com.vonHousen.kappusta.MainActivity
import com.vonHousen.kappusta.db.ReportDAO
import com.vonHousen.kappusta.db.ReportEntity

object PaymentRepository {
    private val reportDAO: ReportDAO = MainActivity.db.reportDAO()

    fun add(paymentRecord: PaymentRecord) {
        reportDAO.insertReport(ReportEntity(paymentRecord))
    }

    fun getAll(): List<PaymentRecord> {
        val reports = mutableListOf<PaymentRecord>()
        val loadedReports = reportDAO.allReports
        if (loadedReports != null) {
            for (report in loadedReports) {
                reports.add(PaymentRecord(report))
            }
        }
        return reports
    }
}