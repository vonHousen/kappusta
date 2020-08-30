package com.vonHousen.kappusta.ui.wallets

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.vonHousen.kappusta.R


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
    }

}
