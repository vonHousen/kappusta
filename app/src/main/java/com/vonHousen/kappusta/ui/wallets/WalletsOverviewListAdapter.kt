package com.vonHousen.kappusta.ui.wallets

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vonHousen.kappusta.walletSharing.WalletOverview


class WalletsOverviewListAdapter(
    private val list: MutableList<WalletOverview>,
    private val whomNotify: WalletsOverviewViewModel,
    private val onClickListener: (View, WalletOverview) -> Unit
) : RecyclerView.Adapter<WalletsOverviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletsOverviewViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return WalletsOverviewViewHolder(
            inflater,
            parent
        )
    }

    override fun onBindViewHolder(holderOverview: WalletsOverviewViewHolder, position: Int) {
        val walletOverview = list[position]
        holderOverview.bind(walletOverview)

        holderOverview.itemView.setOnClickListener { view ->
            onClickListener.invoke(view, walletOverview)
        }
    }

    override fun getItemCount(): Int = list.size

    fun removeAt(position: Int) {
        list.removeAt(position)
//        whomNotify.notifyReportRemoved(position)      TODO implement removing
        notifyItemRemoved(position)
    }
}