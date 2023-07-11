package com.katy.weatherforecastapp.repository

import android.content.Context
import android.util.Log
import androidx.annotation.VisibleForTesting
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
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject


class WeatherRepositoryImpl @Inject constructor(
    private val weatherDataDao: WeatherDataDao,
    private val openWeatherApi: OpenWeatherApi,
    private val networkUtils: NetworkUtils,
    @ApplicationContext private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) :
    WeatherRepository {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun cacheFiveDayForecastList(
        forecast: List<List<WeatherData>>,
        zipcode: String
    ) =
        withContext(ioDispatcher) {
            forecast.flatten().forEach { weatherDataDao.addWeatherData(it.asEntity(zipcode)) }
        }

    override suspend fun getFiveDayForecastListFlow(
        location: Location,
        errorCallbacks: WeatherDataErrorCallbacks
    ): Flow<List<List<WeatherData>>> {
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
            .filterNotNull()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun fetchFiveDayForecast(
        location: Location,
        callbacks: WeatherDataErrorCallbacks
    ) {
        withContext(Dispatchers.IO) {
            val receivedWeather = openWeatherApi.getFiveDayForecast(location.lat, location.lon)
            if (receivedWeather is NetworkResult.Success) {
                val response = receivedWeather.response
                if (response is List<*>
                    && response.isNotEmpty()
                    && response.all { it is List<*> && it.isNotEmpty() && it.all { data -> data is WeatherData } }
                ) {
                    try {
                        val data = receivedWeather.response as List<List<WeatherData>>
                        cacheFiveDayForecastList(
                            data,
                            location.zipcode
                        )
                    } catch (e: Exception) {
                        Log.e("WeatherRepositoryImpl", e.localizedMessage as String)
                        callbacks.onNetworkError()
                    }
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