package com.katy.weatherforecastapp.di

import android.content.Context
import com.katy.weatherforecastapp.App
import com.katy.weatherforecastapp.database.WeatherDatabase
import com.katy.weatherforecastapp.database.dao.LocationDao
import com.katy.weatherforecastapp.database.dao.WeatherDataDao
import com.katy.weatherforecastapp.repository.WeatherRepository
import com.katy.weatherforecastapp.repository.WeatherRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(ViewModelComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindWeatherRepository(implementation: WeatherRepositoryImpl): WeatherRepository


}