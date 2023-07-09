package com.katy.weatherforecastapp.network

import com.katy.weatherforecastapp.BuildConfig
import com.katy.weatherforecastapp.di.IoDispatcher
import com.katy.weatherforecastapp.model.FiveDayForecast
import com.katy.weatherforecastapp.model.WeatherData
import com.katy.weatherforecastapp.model.remote.NetworkLocation
import com.katy.weatherforecastapp.model.remote.asEntity
import com.katy.weatherforecastapp.util.Utils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OpenWeatherApi @Inject constructor(
    private val apiService: OpenWeatherApiService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun getFiveDayForecast(
        latitude: String,
        longitude: String,
    ): List<List<WeatherData>>? {
        return try {
            val response = withContext(ioDispatcher) {
                apiService.getFiveDayForecast(
                    latitude, longitude, BuildConfig.WEATHER_API_KEY, "imperial"
                )
            }
            if (response.isSuccessful) {
                response.body()?.let { Utils.organizeWeatherDataByDay(resolveTimeZone(it)) }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getLatLong(zipCode: String): NetworkResult<*> {
        return try {
            val response = withContext(ioDispatcher) {
                apiService.getLatLon(
                    zipCode, BuildConfig.WEATHER_API_KEY
                )
            }
            if (response.isSuccessful && response.body() != null) {
                NetworkResult.Success((response.body() as NetworkLocation).asEntity(zipCode))
            } else if (response.code() == 404) {
                NetworkResult.BadRequest
            } else {
                NetworkResult.NetworkError
            }
        } catch (e: Exception) {
            NetworkResult.NetworkError
        }
    }

    private fun resolveTimeZone(fiveDayForecast: FiveDayForecast): List<WeatherData> {
        val offset = fiveDayForecast.city.timezone
        val list = fiveDayForecast.list
        list.forEach { it.dtTxt = Utils.convertToLocalTime(it.dtTxt, offset) }
        return list
    }

}