package com.katy.weatherforecastapp

import android.app.Application
import com.katy.weatherforecastapp.database.WeatherDatabase
import com.katy.weatherforecastapp.network.OpenWeatherApi
import com.katy.weatherforecastapp.network.buildApiService
import com.katy.weatherforecastapp.repository.WeatherRepository
import com.katy.weatherforecastapp.repository.WeatherRepositoryImpl

class App: Application() {
    companion object {
        private lateinit var instance: App
        private val apiService by lazy { buildApiService() }
        val openWeatherApi by lazy { OpenWeatherApi(apiService) }
        private val database: WeatherDatabase by lazy {
            WeatherDatabase.buildDatabase(instance)
        }
        val repository:WeatherRepository by lazy {
            WeatherRepositoryImpl(database.locationDao())
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}