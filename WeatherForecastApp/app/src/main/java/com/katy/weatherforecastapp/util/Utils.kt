package com.katy.weatherforecastapp.util

import android.content.Context
import com.katy.weatherforecastapp.R
import com.katy.weatherforecastapp.model.WeatherData
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Utils {

    fun formatDate(date: LocalDateTime, context: Context): String {
        val today = LocalDateTime.now()
        return if (today.dayOfYear == date.dayOfYear) {
            context.getString(R.string.today)
        } else if (today.plusDays(1L).dayOfYear == date.dayOfYear) {
            context.getString(R.string.tomorrow)
        } else if (today.minusDays(1L).dayOfYear == date.dayOfYear) {
            context.getString(R.string.yesterday)
        } else {
            date.dayOfWeek.name.capitalize()
        }
    }

    fun formatTime(date: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("h:mm a")
        return date.format(formatter)
    }

}

fun String.capitalize(): String = this.lowercase().replaceFirstChar { it.titlecase() }