package com.katy.weatherforecastapp.repository

import android.content.Context
import com.katy.weatherforecastapp.database.dao.WeatherDataDao
import com.katy.weatherforecastapp.di.IoDispatcher
import com.katy.weatherforecastapp.model.Location
import com.katy.weatherforecastapp.model.WeatherData
import com.katy.weatherforecastapp.model.asEntity
import com.katy.weatherforecastapp.model.local.sortWeatherDataEntitiesByTimeStamp
import com.katy.weatherforecastapp.model.organizeWeatherDataByDay
import com.katy.weatherforecastapp.network.NetworkResult
import com.katy.weatherforecastapp.network.NetworkUtils
import com.katy.weatherforecastapp.network.OpenWeatherApi
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject


class WeatherRepositoryImpl @Inject constructor(
    private val weatherDataDao: WeatherDataDao,
    private val networkUtils: NetworkUtils,
    private val openWeatherApi: OpenWeatherApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @ApplicationContext private val context: Context
) :
    WeatherRepository {
    private suspend fun cacheFiveDayForecastList(
        forecast: List<List<WeatherData>>,
        zipcode: String
    ) =
        withContext(ioDispatcher) {
            forecast.flatten().forEach { weatherDataDao.addWeatherData(it.asEntity(zipcode)) }
        }

    override suspend fun getFiveDayForecastListFlow(
        location: Location,
        errorCallbacks: WeatherDataErrorCallbacks
    ): Flow<List<List<WeatherData>>?> {
        val flow = weatherDataDao.getWeatherData(location.zipcode)
        val first = flow.first()
        if (first.isEmpty()) {
            if (networkUtils.hasInternetAccess(context)) {
                fetchFiveDayForecast(location, errorCallbacks)
            } else {
                errorCallbacks.onNoInternetNoData()
            }
        }
        return flow.map { organizeWeatherDataByDay(sortWeatherDataEntitiesByTimeStamp(it)) }
    }

    private suspend fun fetchFiveDayForecast(
        location: Location,
        callbacks: WeatherDataErrorCallbacks
    ) {
        withContext(Dispatchers.IO) {
            val receivedWeather = openWeatherApi.getFiveDayForecast(location.lat, location.lon)
            if (receivedWeather is NetworkResult.Success) {
                val response = receivedWeather.response
                if (response is List<*> && response.all { it is List<*> && it.all { it is WeatherData && response.isNotEmpty() } }) {
                    cacheFiveDayForecastList(
                        receivedWeather.response as List<List<WeatherData>>,
                        location.zipcode
                    )
                } else {
                    callbacks.onNetworkError()
                }
            } else {
                callbacks.onNetworkError()
            }
        }
    }

    override suspend fun deleteAllWeatherData() {
        weatherDataDao.deleteAll()
    }

}