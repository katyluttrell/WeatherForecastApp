package com.katy.weatherforecastapp.repository

import com.katy.weatherforecastapp.database.dao.WeatherDataDao
import com.katy.weatherforecastapp.model.WeatherData
import com.katy.weatherforecastapp.model.asEntity
import com.katy.weatherforecastapp.model.local.sortWeatherDataEntitiesByTimeStamp
import com.katy.weatherforecastapp.model.organizeWeatherDataByDay
import javax.inject.Inject


class WeatherRepositoryImpl @Inject constructor(private val weatherDataDao: WeatherDataDao) :
    WeatherRepository {
    override suspend fun cacheFiveDayForecastList(forecast: List<List<WeatherData>>) {
        forecast.flatten().forEach { weatherDataDao.addWeatherData(it.asEntity()) }
    }

    override suspend fun getFiveDayForecastList(): List<List<WeatherData>>? {
        val data = weatherDataDao.getWeatherData()
        return if (data.isEmpty()) {
            null
        } else {
            organizeWeatherDataByDay(sortWeatherDataEntitiesByTimeStamp(data))
        }
    }

    override suspend fun deleteAllWeatherData() {
        weatherDataDao.deleteAll()
    }

}