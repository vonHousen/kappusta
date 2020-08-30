package com.vonHousen.kappusta.ui.wallets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vonHousen.kappusta.etc.CategoriesParser
import com.vonHousen.kappusta.reporting.Money
import com.vonHousen.kappusta.walletSharing.WalletOverview

class WalletsViewModel : ViewModel() {

    private lateinit var categoriesParser: CategoriesParser
    private val _walletsList = MutableLiveData<List<WalletOverview>>().apply {
        value = listOf<WalletOverview>(
            WalletOverview(null, Money(1), "Codzienne", "Z Oli", 1),
            WalletOverview(null, Money(-1), "Specjalne", "Z Oli", 2),
            WalletOverview(null, Money(1), "Inne", "Z Oli", 3)
        )  // TODO delete it
    }
    val walletsList: LiveData<List<WalletOverview>>
        get() = _walletsList

    fun setCategoriesParser(categoriesParser: CategoriesParser) {
        this.categoriesParser = categoriesParser
    }

    fun getWalletsWithParsedCategories(walletList: List<WalletOverview>)
            : MutableList<WalletOverview> {
        val overviewList = walletList.map{ it.copy() }.toMutableList()
        for (wallet in overviewList) {
            wallet.category = categoriesParser.convertToLocalCategoryName(wallet.category)
        }
        return overviewList
    }
}