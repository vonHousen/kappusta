package com.vonHousen.kappusta.ui.wallets

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.vonHousen.kappusta.etc.CategoriesParser
import com.vonHousen.kappusta.reporting.Money
import com.vonHousen.kappusta.walletSharing.UsersWallet
import com.vonHousen.kappusta.walletSharing.Wallet
import com.vonHousen.kappusta.walletSharing.WalletOverview

class WalletViewModel : ViewModel() {

    private lateinit var parentFragment: WalletFragment

    private val db = Firebase.database

    fun setParent(walletFragment: WalletFragment) {
        parentFragment = walletFragment
    }

}