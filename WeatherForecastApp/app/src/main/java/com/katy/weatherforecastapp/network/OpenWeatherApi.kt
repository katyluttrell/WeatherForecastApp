package com.katy.weatherforecastapp.network

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import com.katy.weatherforecastapp.BuildConfig
import com.katy.weatherforecastapp.model.FiveDayForecast
import com.katy.weatherforecastapp.model.LatLonResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OpenWeatherApi(private val apiService: OpenWeatherApiService) {

    fun getFiveDayForecast(latitude:Double, longitude:Double, callback: (Response<FiveDayForecast>) -> Unit){
        apiService.getFiveDayForecast(44.34,10.99, BuildConfig.WEATHER_API_KEY, "imperial")
            .enqueue(object: Callback<FiveDayForecast>{
                override fun onResponse(
                    call: Call<FiveDayForecast>,
                    response: Response<FiveDayForecast>
                ) {
                   Log.d("DEBUG", response.toString())
                    callback(response)
                }

                override fun onFailure(call: Call<FiveDayForecast>, t: Throwable) {
                    Log.d("DEBUG", t.toString())
                }

            })
    }

    fun getLatLong(zipCode:String, context: Context){
        apiService.getLatLon(zipCode, BuildConfig.WEATHER_API_KEY)
            .enqueue(object :Callback<LatLonResponse>{
                override fun onResponse(
                    call: Call<LatLonResponse>,
                    response: Response<LatLonResponse>
                ) {
                    AlertDialog.Builder(context).setTitle(response.body()?.locationName ?: "Not a real zipcode")
                        .show()
                }

                override fun onFailure(call: Call<LatLonResponse>, t: Throwable) {
                    Log.d("DEBUG", t.toString())
                }

            })
    }

}