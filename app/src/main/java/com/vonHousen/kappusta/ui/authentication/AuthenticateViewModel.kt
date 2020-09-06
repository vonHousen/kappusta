package com.vonHousen.kappusta.ui.authentication

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.vonHousen.kappusta.etc.HashUtils

class AuthenticateViewModel : ViewModel() {

    private var loggedInUser: FirebaseUser? = null

    fun setUser(user: FirebaseUser) {
        loggedInUser = user
    }

    fun getUserUidHashed(): String {
        return HashUtils.sha256(loggedInUser!!.uid)
    }

    fun getUserUid(): String {
        return loggedInUser!!.uid
    }

    fun getUserEmail(): String {
        return loggedInUser!!.email!!
    }

    fun withdrawUser() {
        loggedInUser = null
    }
}