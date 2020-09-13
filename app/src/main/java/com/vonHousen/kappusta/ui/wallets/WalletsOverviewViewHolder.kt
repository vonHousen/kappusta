package com.vonHousen.kappusta.ui.wallets

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vonHousen.kappusta.R
import com.vonHousen.kappusta.walletSharing.WalletOverview


class WalletsOverviewViewHolder(inflater: LayoutInflater, parent: ViewGroup)
    : RecyclerView.ViewHolder(
        inflater.inflate(R.layout.fragment_wallets_overview_item_row, parent, false)
    ) {
    private var nameField: TextView? = null
    private var categoryField: TextView? = null
    private var lastDateField: TextView? = null
    private var balanceField: TextView? = null

    init {
        nameField = itemView.findViewById(R.id.wallets_overview_item_wallet_name)
        categoryField = itemView.findViewById(R.id.wallets_overview_item_category)
        lastDateField = itemView.findViewById(R.id.wallets_overview_item_last_date)
        balanceField = itemView.findViewById(R.id.wallets_overview_item_balance)
    }

    fun bind(walletOverview: WalletOverview) {
        nameField?.text = walletOverview.getName()
        categoryField?.text = walletOverview.getCategoryNameString()
        lastDateField?.text = walletOverview.getLastDateString()
        balanceField?.text = walletOverview.getBalanceString()
    }
}