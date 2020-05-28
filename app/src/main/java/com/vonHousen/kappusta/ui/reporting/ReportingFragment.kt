package com.vonHousen.kappusta.ui.reporting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.vonHousen.kappusta.R
import kotlinx.android.synthetic.main.fragment_reporting.*

class ReportingFragment : Fragment() {

    private lateinit var reportingViewModel: ReportingViewModel

    companion object {
        fun newInstance() = ReportingFragment()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reporting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        reportingViewModel =
            ViewModelProviders.of(this).get(ReportingViewModel::class.java)

        reportingViewModel.text.observe(viewLifecycleOwner, Observer {
            text_reporting.text = it
        })

        button_reporting_ok.setOnClickListener {
            (activity as AppCompatActivity).supportFragmentManager
                .popBackStackImmediate()

        }
    }

}
