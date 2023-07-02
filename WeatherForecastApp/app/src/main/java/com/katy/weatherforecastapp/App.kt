package com.katy.weatherforecastapp

import android.app.Application
import com.katy.weatherforecastapp.Network.OpenWeatherApi
import com.katy.weatherforecastapp.Network.buildApiService

class App: Application() {
    companion object {
        private val apiService by lazy { buildApiService() }
        val openWeatherApi by lazy { OpenWeatherApi(apiService) }
    }
}