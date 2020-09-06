package com.vonHousen.kappusta.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.vonHousen.kappusta.MainActivity
import com.vonHousen.kappusta.R
import com.vonHousen.kappusta.reporting.Money
import com.vonHousen.kappusta.ui.history.HistoryViewModel
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var historyViewModel: HistoryViewModel


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        settingsViewModel =
            ViewModelProviders.of(this).get(SettingsViewModel::class.java)
        historyViewModel =
            ViewModelProviders.of(requireActivity()).get(HistoryViewModel::class.java)

        configureSettingBudgetFields()
        configureLogoutButton()
    }

    private fun configureSettingBudgetFields() {
        historyViewModel.moneyBudget.observe(viewLifecycleOwner, Observer {
             settingsViewModel.updateBudgetTxt(it)
        })
        settingsViewModel.textMoneyBudget.observe(viewLifecycleOwner, Observer {
            setting_budget_edit_text.editText?.setText(it)
        })
        setting_budget_edit_text.setEndIconOnClickListener {
            val inputVal = Money(setting_budget_edit_text.editText?.text.toString())
            val oldVal = historyViewModel.moneyBudget.value
            if (inputVal != oldVal)
                settingsViewModel.updateBudgetTxt(inputVal)
                historyViewModel.updateBudget(inputVal)
            (activity as MainActivity).hideKeyboardPublic()
        }
    }

    private fun configureLogoutButton() {
        button_logout.setOnClickListener {  // TODO configure correct logout
//            val parentActivity = (activity as MainActivity)
//            parentActivity.goToLoginFragment(doSignOut = true)
        }
    }
}
