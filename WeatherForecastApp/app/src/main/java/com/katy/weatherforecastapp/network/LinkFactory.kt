package com.katy.weatherforecastapp.network

import android.net.Uri
import androidx.core.net.toUri

const val openWeatherIconURL = "https://openweathermap.org/img/wn/{icon}@2x.png"
class LinkFactory {
    fun openWeatherIconLink(icon:String): String = openWeatherIconURL.replace("{icon}", icon)
}