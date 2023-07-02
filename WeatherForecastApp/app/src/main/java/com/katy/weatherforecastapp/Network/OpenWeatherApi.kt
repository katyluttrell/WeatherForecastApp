package com.katy.weatherforecastapp.Network

import android.util.Log
import com.katy.weatherforecastapp.BuildConfig
import com.katy.weatherforecastapp.Responses.FiveDayForecastResponse
import com.katy.weatherforecastapp.Responses.LatLonResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OpenWeatherApi(private val apiService: OpenWeatherApiService) {

    fun getFiveDayForeCast(){
        apiService.getFiveDayForecast(44.34,10.99, BuildConfig.WEATHER_API_KEY)
            .enqueue(object: Callback<FiveDayForecastResponse>{
                override fun onResponse(
                    call: Call<FiveDayForecastResponse>,
                    response: Response<FiveDayForecastResponse>
                ) {
                   Log.d("DEBUG", response.toString())
                }

                override fun onFailure(call: Call<FiveDayForecastResponse>, t: Throwable) {
                    Log.d("DEBUG", t.toString())
                }

            })
    }

    fun getLatLong(){
        apiService.getLatLon("80303", BuildConfig.WEATHER_API_KEY)
            .enqueue(object :Callback<LatLonResponse>{
                override fun onResponse(
                    call: Call<LatLonResponse>,
                    response: Response<LatLonResponse>
                ) {
                    Log.d("DEBUG", response.toString())
                }

                override fun onFailure(call: Call<LatLonResponse>, t: Throwable) {
                    Log.d("DEBUG", t.toString())
                }

            })
    }

}