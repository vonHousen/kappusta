package com.vonHousen.kappusta.walletSharing

data class Wallet (
    val name: String,
    val category: String
) {
    constructor(): this("", "")
}