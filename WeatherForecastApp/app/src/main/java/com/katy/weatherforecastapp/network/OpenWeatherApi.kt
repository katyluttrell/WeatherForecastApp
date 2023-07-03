package com.katy.weatherforecastapp.network

import android.util.Log
import com.katy.weatherforecastapp.BuildConfig
import com.katy.weatherforecastapp.model.FiveDayForecast
import com.katy.weatherforecastapp.model.LatLonResponse
import com.katy.weatherforecastapp.model.WeatherData
import com.katy.weatherforecastapp.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OpenWeatherApi(private val apiService: OpenWeatherApiService) {

    fun getFiveDayForecast(latitude:String, longitude:String, callback: (List<List<WeatherData>>) -> Unit){
        apiService.getFiveDayForecast(latitude,longitude, BuildConfig.WEATHER_API_KEY, "imperial")
            .enqueue(object: Callback<FiveDayForecast>{
                override fun onResponse(
                    call: Call<FiveDayForecast>,
                    response: Response<FiveDayForecast>
                ) {
                   Log.d("DEBUG", response.toString())
                    response.body()?.let { callback(organizeByDay(it))}
                }

                override fun onFailure(call: Call<FiveDayForecast>, t: Throwable) {
                    Log.d("DEBUG", t.toString())
                }

            })
    }

    fun getLatLong(zipCode:String, callback: (LatLonResponse) -> Unit){
        apiService.getLatLon(zipCode, BuildConfig.WEATHER_API_KEY)
            .enqueue(object :Callback<LatLonResponse>{
                override fun onResponse(
                    call: Call<LatLonResponse>,
                    response: Response<LatLonResponse>
                ) {
                    response.body()?.let { callback(it) }
                }

                override fun onFailure(call: Call<LatLonResponse>, t: Throwable) {
                    Log.d("DEBUG", t.toString())
                }

            })
    }

    private fun resolveTimeZone(fiveDayForecast: FiveDayForecast): List<WeatherData>{
        val offset = fiveDayForecast.city.timezone
        val list = fiveDayForecast.list
        list.forEach{ it.dtTxt = Utils.convertToLocalTime(it.dtTxt, offset) }
        return list
    }

    private fun organizeByDay(fiveDayForecast: FiveDayForecast): List<List<WeatherData>>{
        val resolvedList = resolveTimeZone(fiveDayForecast)
        val listOfDays = mutableListOf<List<WeatherData>>()
        var oneDayForecastList = mutableListOf<WeatherData>()
        var currentDay: Int? = null
        for (weatherData in resolvedList) {
            val dataDay = weatherData.dtTxt.dayOfYear
            if(dataDay != currentDay){
                if(oneDayForecastList.isNotEmpty()){
                    listOfDays.add(oneDayForecastList)
                    oneDayForecastList = mutableListOf<WeatherData>()
                }
                currentDay = dataDay
            }
            oneDayForecastList.add(weatherData)
        }
        return listOfDays
    }
}