package com.vonHousen.kappusta.walletSharing

data class UsersWallet (
    val enabled: Boolean
) {
    constructor(): this(false)
}