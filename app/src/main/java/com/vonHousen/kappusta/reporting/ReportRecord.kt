package com.vonHousen.kappusta.reporting

import java.time.LocalDate

data class ReportRecord(
    var DATE: LocalDate?,
    var WORTH: Double,
    var COMMENT: String?,
    var ID: Long
) {

    // TODO implement explicit constructor

    fun getWorthString(): String {
        return "$WORTH zł"    // TODO formatting
    }

    fun getDateString(): String {
        return if (DATE != null) {
            DATE.toString()
        } else {
            "unknown date"
        }
    }

    fun getCommentString(): String {
        return if (COMMENT != null) {
            COMMENT.toString()      // TODO bound with string.xml
        } else {
            "unknown category"
        }
    }
}

