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
import kotlinx.android.synthetic.main.fragment_wallets.*


class WalletsFragment : Fragment() {

    private lateinit var walletsViewModel: WalletsViewModel
    private lateinit var authViewModel: AuthenticateViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wallets, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel =
            ViewModelProviders.of(requireActivity()).get(AuthenticateViewModel::class.java)
        walletsViewModel =
            ViewModelProviders.of(this).get(WalletsViewModel::class.java)
        walletsViewModel.setCategoriesParser(
            CategoriesParser(resources.getStringArray(R.array.Categories))
        )
        walletsViewModel.setParent(this)
        walletsViewModel.registerUser(
            uid = authViewModel.getUserUid(),
            email = authViewModel.getUserEmail()
        )
        walletsViewModel.walletsList.observe(viewLifecycleOwner, Observer {
            updateRecyclerView(it)
        })
    }

    private fun updateRecyclerView(walletsList: List<WalletOverview>?) {
        if (walletsList != null) {

            val actionOnClick: (View, WalletOverview) -> Unit = { view, walletOverview ->
                toastMessage("No touching!")
            }

            val wallets = walletsViewModel.getWalletsWithParsedCategories(walletsList)
            val customAdapter = WalletsListAdapter(wallets, walletsViewModel, actionOnClick)
            wallets_recycler_view.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = customAdapter
            }
        }
    }

    fun toastMessage(msg: String) {
        val parentActivity = activity as MainActivity
        Toast.makeText(parentActivity, msg, Toast.LENGTH_SHORT).show()
    }
}
