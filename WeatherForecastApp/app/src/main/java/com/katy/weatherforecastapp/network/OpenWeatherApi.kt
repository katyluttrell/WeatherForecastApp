package com.katy.weatherforecastapp.network

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import com.katy.weatherforecastapp.BuildConfig
import com.katy.weatherforecastapp.model.FiveDayForecast
import com.katy.weatherforecastapp.model.LatLonResponse
import com.katy.weatherforecastapp.model.WeatherData
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
                    response.body()?.list?.let { callback(it) }
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
}