package com.katy.weatherforecastapp.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.katy.weatherforecastapp.model.LatLonResponse

class MainViewModel : ViewModel() {

    val latLonResponse: MutableLiveData<LatLonResponse> by lazy {
        MutableLiveData<LatLonResponse>()
    }
}