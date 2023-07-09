package com.katy.weatherforecastapp.di

import android.content.Context
import com.katy.weatherforecastapp.database.WeatherDatabase
import com.katy.weatherforecastapp.database.dao.LocationDao
import com.katy.weatherforecastapp.database.dao.WeatherDataDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    fun provideLocationDao(database: WeatherDatabase): LocationDao {
        return database.locationDao()
    }

    @Provides
    fun provideWeatherDataDao(database: WeatherDatabase): WeatherDataDao {
        return database.weatherDataDao()
    }

    @Provides
    fun provideDatabase(@ApplicationContext applicationContext: Context): WeatherDatabase {
        return WeatherDatabase.buildDatabase(applicationContext)
    }
}