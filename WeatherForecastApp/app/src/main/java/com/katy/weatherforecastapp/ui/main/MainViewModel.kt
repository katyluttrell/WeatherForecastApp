package com.katy.weatherforecastapp.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.katy.weatherforecastapp.model.Location
import com.katy.weatherforecastapp.model.WeatherData

class MainViewModel : ViewModel() {

    val location: MutableLiveData<Location> by lazy {
        MutableLiveData<Location>()
    }

    val weatherDataList: MutableLiveData<List<List<WeatherData>>> by lazy {
        MutableLiveData<List<List<WeatherData>>>()
    }

    var noInternetAlertShown = false
}