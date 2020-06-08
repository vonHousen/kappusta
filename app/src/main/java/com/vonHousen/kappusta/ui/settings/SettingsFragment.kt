package com.vonHousen.kappusta.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.vonHousen.kappusta.R
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel

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

        configureSettingBudgetFields()
    }

    private fun configureSettingBudgetFields() {
        settingsViewModel.textMoneyBudget.observe(viewLifecycleOwner, Observer {
                setting_budget_edit_text.editText?.setText(it)
        })
        setting_budget_edit_text.setEndIconOnClickListener {
            val inputTxt = setting_budget_edit_text.editText?.text.toString()
            val oldTxt = settingsViewModel.textMoneyBudget.value.toString()
            if (inputTxt != oldTxt)     // TODO need to compare real currency here
                settingsViewModel.updateBudgetTxt(inputTxt.toDouble())
        }
    }

}
