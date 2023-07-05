package com.katy.weatherforecastapp.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.katy.weatherforecastapp.model.Location

class MainViewModel : ViewModel() {

    val location: MutableLiveData<Location> by lazy {
        MutableLiveData<Location>()
    }
}