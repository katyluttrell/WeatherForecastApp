package com.katy.weatherforecastapp.repository

import com.katy.weatherforecastapp.database.dao.WeatherDataDao
import com.katy.weatherforecastapp.model.WeatherData
import com.katy.weatherforecastapp.util.Utils
import javax.inject.Inject


class WeatherRepositoryImpl @Inject constructor(private val weatherDataDao: WeatherDataDao) :
    WeatherRepository {
    override suspend fun cacheFiveDayForecastList(forecast: List<List<WeatherData>>) {
        forecast.flatten().forEach { weatherDataDao.addWeatherData(it) }
    }

    override suspend fun getFiveDayForecastList(): List<List<WeatherData>>? {
        return weatherDataDao.getWeatherData()
            ?.let { Utils.organizeWeatherDataByDay(Utils.sortWeatherDataByTimeStamp(it)) }
    }

    override suspend fun deleteAllWeatherData() {
        weatherDataDao.deleteAll()
    }

}