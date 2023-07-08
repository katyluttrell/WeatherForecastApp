package com.katy.weatherforecastapp

import android.app.Application
import com.katy.weatherforecastapp.database.WeatherDatabase
import com.katy.weatherforecastapp.network.OpenWeatherApi
import com.katy.weatherforecastapp.network.buildApiService
import com.katy.weatherforecastapp.repository.WeatherRepository
import com.katy.weatherforecastapp.repository.WeatherRepositoryImpl
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {
}