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


class WalletsOverviewFragment : Fragment() {

    private lateinit var walletsOverviewViewModel: WalletsOverviewViewModel
    private lateinit var authViewModel: AuthenticateViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wallets_overview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel =
            ViewModelProviders.of(requireActivity()).get(AuthenticateViewModel::class.java)
        walletsOverviewViewModel =
            ViewModelProviders.of(this).get(WalletsOverviewViewModel::class.java)
        walletsOverviewViewModel.setCategoriesParser(
            CategoriesParser(resources.getStringArray(R.array.Categories))
        )
        walletsOverviewViewModel.setParent(this)
        walletsOverviewViewModel.registerUser(
            uid = authViewModel.getUserUid(),
            email = authViewModel.getUserEmail()
        )
        walletsOverviewViewModel.walletsList.observe(viewLifecycleOwner, Observer {
            updateRecyclerView(it)
        })
    }

    private fun updateRecyclerView(walletsList: List<WalletOverview>?) {
        if (walletsList != null) {
            val actionOnClick: (View, WalletOverview) -> Unit = { view, walletOverview ->
                openWallet(walletOverview)
            }
            val wallets = walletsOverviewViewModel.getWalletsWithParsedCategories(walletsList)
            val customAdapter =
                WalletsOverviewListAdapter(wallets, walletsOverviewViewModel, actionOnClick)
            wallets_overview_recycler_view.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = customAdapter
            }
        }
    }

    fun toastMessage(msg: String) {
        val parentActivity = activity as MainActivity
        Toast.makeText(parentActivity, msg, Toast.LENGTH_SHORT).show()
    }

    private fun openWallet(walletOverview: WalletOverview) {
        toastMessage("No touching yet!")    // TODO replace with opening WalletFragment
    }
}
