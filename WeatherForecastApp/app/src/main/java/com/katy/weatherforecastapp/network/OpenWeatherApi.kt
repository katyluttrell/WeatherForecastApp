package com.katy.weatherforecastapp.network

import com.katy.weatherforecastapp.BuildConfig
import com.katy.weatherforecastapp.di.IoDispatcher
import com.katy.weatherforecastapp.model.remote.NetworkFiveDayForecast
import com.katy.weatherforecastapp.model.remote.NetworkLocation
import com.katy.weatherforecastapp.model.remote.asExternalModel
import com.katy.weatherforecastapp.model.remote.asOrganizedWeatherDataList
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
    ): NetworkResult<*> {
        return try {
            val response = withContext(ioDispatcher) {
                apiService.getFiveDayForecast(
                    latitude, longitude, BuildConfig.WEATHER_API_KEY, "imperial"
                )
            }
            if (response.isSuccessful) {
                NetworkResult.Success((response.body() as NetworkFiveDayForecast).asOrganizedWeatherDataList())
            } else {
                NetworkResult.NetworkError
            }
        } catch (e: Exception) {
            NetworkResult.NetworkError
        }
    }

    suspend fun getLocation(zipCode: String): NetworkResult<*> {
        return try {
            val response = withContext(ioDispatcher) {
                apiService.getLatLon(
                    zipCode, BuildConfig.WEATHER_API_KEY
                )
            }
            if (response.isSuccessful && response.body() != null) {
                NetworkResult.Success((response.body() as NetworkLocation).asExternalModel(zipCode))
            } else if (response.code() == 404) {
                NetworkResult.BadRequest
            } else {
                NetworkResult.NetworkError
            }
        } catch (e: Exception) {
            NetworkResult.NetworkError
        }
    }
}