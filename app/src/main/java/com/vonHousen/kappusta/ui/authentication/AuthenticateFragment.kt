package com.vonHousen.kappusta.ui.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.vonHousen.kappusta.R

class AuthenticateFragment : Fragment() {

    private lateinit var authViewModel: AuthenticateViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_authentication, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        authViewModel =
            ViewModelProviders.of(this).get(AuthenticateViewModel::class.java)

    }
}