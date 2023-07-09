package com.katy.weatherforecastapp.di

import com.katy.weatherforecastapp.network.OpenWeatherApi
import com.katy.weatherforecastapp.network.OpenWeatherApiService
import com.katy.weatherforecastapp.network.buildApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    @Provides
    fun provideOpenWeatherApi(
        apiService: OpenWeatherApiService,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): OpenWeatherApi {
        return OpenWeatherApi(apiService, ioDispatcher)
    }

    @Provides
    fun provideOpenWeatherApiService(): OpenWeatherApiService {
        return buildApiService()
    }
}