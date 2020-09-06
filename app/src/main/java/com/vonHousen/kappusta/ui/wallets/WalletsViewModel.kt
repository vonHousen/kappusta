package com.vonHousen.kappusta.ui.wallets

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

class WalletsViewModel : ViewModel() {

    private lateinit var categoriesParser: CategoriesParser
    private lateinit var parentFragment: WalletsFragment

    private val db = Firebase.database

    private val _walletsListValues = ArrayList<WalletOverview>()
    private val _walletsList = MutableLiveData<List<WalletOverview>>()
    val walletsList: LiveData<List<WalletOverview>>
        get() = _walletsList
    private fun addToLocalWallets(walletOverview: WalletOverview, doNotify: Boolean) {
        _walletsListValues.add(walletOverview)
        if (doNotify)
            _walletsList.value = _walletsListValues
    }
    private fun removeFromLocalWallets(walletID: String, doNotify: Boolean) {
        val didRemove = _walletsListValues.removeIf {wallet -> wallet.walletID == walletID}
        if (didRemove and doNotify)
            _walletsList.value = _walletsListValues
    }

    private val usersWallets = hashMapOf<String, UsersWallet>()
    private val walletsSubscriptions = hashMapOf<String, ValueEventListener>()

    fun setCategoriesParser(categoriesParser: CategoriesParser) {
        this.categoriesParser = categoriesParser
    }

    fun setParent(walletsFragment: WalletsFragment) {
        parentFragment = walletsFragment
    }

    fun getWalletsWithParsedCategories(walletList: List<WalletOverview>)
            : MutableList<WalletOverview> {
        val overviewList = walletList.map{ it.copy() }.toMutableList()
        for (wallet in overviewList) {
            wallet.category = categoriesParser.convertToLocalCategoryName(wallet.category)
        }
        return overviewList
    }

    fun registerUser(uid: String, email: String) {
        saveUserIfNotExist(uid, email)
        subscribeUsersWallets(uid)
    }

    private fun saveUserIfNotExist(uid: String, email: String) {
        val thisUserNode = db.getReference("Users/$uid")
        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (! dataSnapshot.exists())
                    thisUserNode.setValue(email)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "registerUser:onCancelled", databaseError.toException())
            }
        }
        thisUserNode.addListenerForSingleValueEvent(userListener)
    }

    private fun subscribeUsersWallets(uid: String) {
        val thisUsersWallets = db.getReference("UsersWallets/$uid")
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val newUsersWallet = dataSnapshot.getValue<UsersWallet>() ?: return
                val newUsersWalletID = dataSnapshot.key ?: return
                usersWallets[newUsersWalletID] = newUsersWallet
                if (newUsersWallet.enabled)
                    subscribeWallet(newUsersWalletID)
            }
            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val usersWallet = dataSnapshot.getValue<UsersWallet>() ?: return
                val walletKey = dataSnapshot.key ?: return
                val didSubscribeWallet = usersWallets[walletKey]!!.enabled
                val doSubscribeWallet = usersWallet.enabled
                if (! didSubscribeWallet and doSubscribeWallet) {
                    subscribeWallet(walletKey)
                } else if (didSubscribeWallet and ! doSubscribeWallet) {
                    stopSubscribingWallet(walletKey)
                }
                usersWallets[walletKey] = usersWallet
            }
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                val walletKey = dataSnapshot.key ?: return
                val didSubscribeWallet = usersWallets[walletKey]!!.enabled
                if (didSubscribeWallet) {
                    stopSubscribingWallet(walletKey)
                }
                usersWallets.remove(walletKey)
            }
            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
            }
            override fun onCancelled(databaseError: DatabaseError) {
                parentFragment.toastMessage("Failed to load user's wallets.")
            }
        }
        thisUsersWallets.addChildEventListener(childEventListener)
    }

    private fun subscribeWallet(walletID: String) {
        val thisUsersWallets = db.getReference("Wallets/$walletID")
        val walletListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val wallet = dataSnapshot.getValue<Wallet>()
                if (wallet == null) {
                    // wallet was removed
                    removeFromLocalWallets(walletID, doNotify = true)
                    return
                }

                removeFromLocalWallets(walletID, doNotify = false)
                addToLocalWallets(
                    WalletOverview(
                        lastDate = null,                    // TODO calculate it
                        balance = Money(0),     // TODO calculate it
                        category = wallet.category,
                        walletName = wallet.name,
                        walletID = walletID
                    ),
                    doNotify = true
                )
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "subscribeWallet:onCancelled", databaseError.toException())
            }
        }
        thisUsersWallets.addValueEventListener(walletListener)
        walletsSubscriptions[walletID] = walletListener
    }

    private fun stopSubscribingWallet(walletID: String) {
        if (walletsSubscriptions.containsKey(walletID)) {
            val thisUsersWallets = db.getReference("Wallets/$walletID")
            thisUsersWallets.removeEventListener(walletsSubscriptions[walletID]!!)
            walletsSubscriptions.remove(walletID)
        }
        removeFromLocalWallets(walletID, doNotify = true)
    }
}