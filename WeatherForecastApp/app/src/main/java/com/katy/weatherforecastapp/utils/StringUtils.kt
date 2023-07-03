package com.katy.weatherforecastapp.utils

import android.content.Context
import com.katy.weatherforecastapp.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class StringUtils(private val context:Context) {

     fun formatDate(dtTxt: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val date = LocalDateTime.parse(dtTxt, formatter)
        val today = LocalDateTime.now()
        return if( today.dayOfYear == date.dayOfYear){
            context.getString(R.string.today)
        } else if (today.plusDays(1L).dayOfYear == date.dayOfYear) {
            context.getString(R.string.tomorrow)
        } else {
            date.dayOfWeek.name.capitalize()
        }
    }
}

fun String.capitalize(): String = this.lowercase().replaceFirstChar { it.titlecase() }