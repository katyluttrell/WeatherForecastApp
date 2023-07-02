package com.katy.weatherforecastapp

import android.app.Application
import com.katy.weatherforecastapp.network.OpenWeatherApi
import com.katy.weatherforecastapp.network.buildApiService

class App: Application() {
    companion object {
        private val apiService by lazy { buildApiService() }
        val openWeatherApi by lazy { OpenWeatherApi(apiService) }
    }
}