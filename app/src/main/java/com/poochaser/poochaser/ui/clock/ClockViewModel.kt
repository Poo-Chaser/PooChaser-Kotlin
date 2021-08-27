package com.poochaser.poochaser.ui.clock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ClockViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is clock Fragment"
    }
    val text: LiveData<String> = _text
}