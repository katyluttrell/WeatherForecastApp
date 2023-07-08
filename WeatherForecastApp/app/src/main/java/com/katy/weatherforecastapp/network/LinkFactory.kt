package com.katy.weatherforecastapp.network

import android.net.Uri
import androidx.core.net.toUri
import javax.inject.Inject

const val openWeatherIconURL = "https://openweathermap.org/img/wn/{icon}@2x.png"
class LinkFactory @Inject constructor(){
    fun openWeatherIconLink(icon:String): String = openWeatherIconURL.replace("{icon}", icon)
}