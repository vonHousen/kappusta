package com.vonHousen.kappusta.ui.reporting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReportingViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is reporting Fragment"
    }
    val text: LiveData<String> = _text
}