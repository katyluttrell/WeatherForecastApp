package com.katy.weatherforecastapp.di

import com.katy.weatherforecastapp.repository.LocationRepository
import com.katy.weatherforecastapp.repository.LocationRepositoryImpl
import com.katy.weatherforecastapp.repository.WeatherRepository
import com.katy.weatherforecastapp.repository.WeatherRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindWeatherRepository(implementation: WeatherRepositoryImpl): WeatherRepository

    @Binds
    abstract fun bindLocationRepository(implementation: LocationRepositoryImpl): LocationRepository


}