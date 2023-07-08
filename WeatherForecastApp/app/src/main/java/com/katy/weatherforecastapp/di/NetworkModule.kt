package com.katy.weatherforecastapp.di

import com.katy.weatherforecastapp.database.WeatherDatabase
import com.katy.weatherforecastapp.database.dao.LocationDao
import com.katy.weatherforecastapp.database.dao.WeatherDataDao
import com.katy.weatherforecastapp.network.OpenWeatherApi
import com.katy.weatherforecastapp.network.OpenWeatherApiService
import com.katy.weatherforecastapp.network.buildApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    @Provides
    fun provideOpenWeatherApi(apiService: OpenWeatherApiService): OpenWeatherApi {
        return OpenWeatherApi(apiService)
    }

    @Provides
    fun provideOpenWeatherApiService(): OpenWeatherApiService {
        return buildApiService()
    }
}