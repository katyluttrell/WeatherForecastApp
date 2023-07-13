package com.katy.weatherforecastapp.util

import android.widget.ImageView
import coil.load
import com.katy.weatherforecastapp.R
import com.katy.weatherforecastapp.model.WeatherData
import com.katy.weatherforecastapp.network.LinkFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RecyclerViewAdapterUtils {

    fun getMiddleOrFirstTime(day: List<WeatherData>): WeatherData? {
        return if (day.isNotEmpty()) {
            if (day.size == 8) {
                day[4]
            } else {
                day[0]
            }
        } else {
            null
        }
    }

    fun setWeatherImage(weatherImage: ImageView, icon: String) {
        val url = LinkFactory().openWeatherIconLink(icon)
        weatherImage.load(url) {
            size(350, 350)
            placeholder(R.drawable.baseline_sync)
            error(R.drawable.baseline_sync_problem_24)
        }
    }

    fun getTempIconColor(temp: Double): Int? {
        return when {
            temp <= 32.0 -> R.color.md_theme_light_primary
            temp >= 85.0 -> R.color.md_theme_light_error
            else -> null
        }
    }

    fun formatDate(date: LocalDateTime, today:LocalDateTime = LocalDateTime.now()): Int? {
        return if (today.dayOfYear == date.dayOfYear) {
            R.string.today
        } else if (today.plusDays(1L).dayOfYear == date.dayOfYear) {
            R.string.tomorrow
        } else {
            null
        }
    }

    fun formatTime(date: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("h:mm a")
        return date.format(formatter)
    }
}