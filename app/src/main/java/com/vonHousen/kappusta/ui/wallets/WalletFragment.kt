package com.vonHousen.kappusta.ui.wallets

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.vonHousen.kappusta.MainActivity
import com.vonHousen.kappusta.R
import com.vonHousen.kappusta.etc.CategoriesParser
import com.vonHousen.kappusta.ui.authentication.AuthenticateViewModel
import com.vonHousen.kappusta.walletSharing.WalletOverview
import kotlinx.android.synthetic.main.fragment_wallets_overview.*


class WalletFragment : Fragment() {

    private lateinit var walletViewModel: WalletViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wallet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        walletViewModel =
            ViewModelProviders.of(this).get(WalletViewModel::class.java)
        walletViewModel.setParent(this)
    }
}
