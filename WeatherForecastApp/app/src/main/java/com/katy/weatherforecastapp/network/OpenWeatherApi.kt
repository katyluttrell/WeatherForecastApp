package com.katy.weatherforecastapp.network

import com.katy.weatherforecastapp.BuildConfig
import com.katy.weatherforecastapp.model.FiveDayForecast
import com.katy.weatherforecastapp.model.Location
import com.katy.weatherforecastapp.model.WeatherData
import com.katy.weatherforecastapp.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OpenWeatherApi @Inject constructor(private val apiService: OpenWeatherApiService) {

    suspend fun getFiveDayForecast(
        latitude: String,
        longitude: String,
    ): List<List<WeatherData>>? {
        return try{
            val response = withContext(Dispatchers.IO){ apiService.getFiveDayForecast(
                latitude,
                longitude,
                BuildConfig.WEATHER_API_KEY,
                "imperial"
            )}
            if (response.isSuccessful) {
                response.body()?.let { Utils.organizeWeatherDataByDay(resolveTimeZone(it)) }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
}

    suspend fun getLatLong(zipCode:String): Location?{
       return  try {
            val response = withContext(Dispatchers.IO) {
                apiService.getLatLon(
                    zipCode,
                    BuildConfig.WEATHER_API_KEY
                )
            }
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        }catch (e: Exception){
            null
        }
    }

    private fun resolveTimeZone(fiveDayForecast: FiveDayForecast): List<WeatherData>{
        val offset = fiveDayForecast.city.timezone
        val list = fiveDayForecast.list
        list.forEach{ it.dtTxt = Utils.convertToLocalTime(it.dtTxt, offset) }
        return list
    }

}