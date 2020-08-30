package com.vonHousen.kappusta.ui.wallets

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vonHousen.kappusta.walletSharing.WalletOverview


class WalletsListAdapter(private val list: MutableList<WalletOverview>,
                         private val whomNotify: WalletsViewModel)
    : RecyclerView.Adapter<WalletsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return WalletsViewHolder(
            inflater,
            parent
        )
    }

    override fun onBindViewHolder(holder: WalletsViewHolder, position: Int) {
        val report = list[position]
        holder.bind(report)
    }

    override fun getItemCount(): Int = list.size

    fun removeAt(position: Int) {
        list.removeAt(position)
//        whomNotify.notifyReportRemoved(position)      TODO implement removing
        notifyItemRemoved(position)
    }
}