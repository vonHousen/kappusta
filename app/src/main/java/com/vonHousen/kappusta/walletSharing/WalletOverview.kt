package com.vonHousen.kappusta.walletSharing

import com.vonHousen.kappusta.reporting.Money
import java.time.LocalDate
import java.time.format.DateTimeFormatter


data class WalletOverview(
    var lastDate: LocalDate?,
    var balance: Money,
    var category: String?,
    var walletName: String,
    var ID: Long
) {

    fun getBalanceString(): String {
        return balance.getTxtWithCurrencyWithSign()
    }

    fun getLastDateString(): String {
        return if (lastDate != null) {
            lastDate!!.format(DateTimeFormatter.ofPattern("dd.MM.yy"))
        } else {
            ""
        }
    }

    fun getCategoryNameString(): String {
        return if (category != null) {
            category.toString()
        } else {
            "Unknown category"
        }
    }

    fun getName(): String {
        return walletName
    }

}

