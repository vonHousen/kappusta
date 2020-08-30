package com.vonHousen.kappusta.ui.wallets

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.vonHousen.kappusta.R
import com.vonHousen.kappusta.etc.CategoriesParser
import com.vonHousen.kappusta.walletSharing.WalletOverview
import kotlinx.android.synthetic.main.fragment_wallets.*


class WalletsFragment : Fragment() {

    private lateinit var walletsViewModel: WalletsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wallets, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        walletsViewModel =
            ViewModelProviders.of(this).get(WalletsViewModel::class.java)
        walletsViewModel.setCategoriesParser(
            CategoriesParser(resources.getStringArray(R.array.Categories))
        )
        walletsViewModel.walletsList.observe(viewLifecycleOwner, Observer {
            updateRecyclerView(it)
        })
    }

    private fun updateRecyclerView(walletsList: List<WalletOverview>?) {
        if (walletsList != null) {
            val wallets = walletsViewModel.getWalletsWithParsedCategories(walletsList)
            val customAdapter = WalletsListAdapter(wallets, walletsViewModel)
            wallets_recycler_view.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = customAdapter
            }
        }
    }


}
