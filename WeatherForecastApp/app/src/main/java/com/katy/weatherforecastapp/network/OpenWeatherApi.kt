package com.katy.weatherforecastapp.network

import android.util.Log
import com.katy.weatherforecastapp.BuildConfig
import com.katy.weatherforecastapp.model.FiveDayForecast
import com.katy.weatherforecastapp.model.Location
import com.katy.weatherforecastapp.model.WeatherData
import com.katy.weatherforecastapp.util.Utils
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
                    response.body()?.let {
                        callback(Utils.organizeWeatherDataByDay(resolveTimeZone(it)))}
                }

                override fun onFailure(call: Call<FiveDayForecast>, t: Throwable) {
                    Log.d("DEBUG", t.toString())
                }

            })
    }

    fun getLatLong(zipCode:String, successCallback: (Location) -> Unit, failureCallback: () -> Unit){
        apiService.getLatLon(zipCode, BuildConfig.WEATHER_API_KEY)
            .enqueue(object :Callback<Location>{
                override fun onResponse(
                    call: Call<Location>,
                    response: Response<Location>
                ) {
                    response.body()?.let { successCallback(it) }
                }

                override fun onFailure(call: Call<Location>, t: Throwable) {
                    Log.d("DEBUG", t.toString())
                    failureCallback()
                }

            })
    }

    private fun resolveTimeZone(fiveDayForecast: FiveDayForecast): List<WeatherData>{
        val offset = fiveDayForecast.city.timezone
        val list = fiveDayForecast.list
        list.forEach{ it.dtTxt = Utils.convertToLocalTime(it.dtTxt, offset) }
        return list
    }

}