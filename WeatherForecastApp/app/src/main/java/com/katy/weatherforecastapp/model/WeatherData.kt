package com.katy.weatherforecastapp.model

import android.os.Parcelable
import com.katy.weatherforecastapp.model.local.MainEntity
import com.katy.weatherforecastapp.model.local.WeatherDataEntity
import com.katy.weatherforecastapp.model.local.WeatherEntity
import com.katy.weatherforecastapp.model.local.WindEntity
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class WeatherData(
    val dtTxt: LocalDateTime,
    val main: Main,
    val weather: Weather,
    val wind: Wind,
):Parcelable

@Parcelize
data class Main(
    val temp: Double,
    val tempMin: Double,
    val tempMax: Double,
    val humidity: String,
):Parcelable

@Parcelize
data class Weather(
    val main: String,
    val description: String,
    val icon: String
): Parcelable

@Parcelize
data class Wind(
    val speed: Double,
    val gust: Double
):Parcelable

fun Main.asEntity(): MainEntity = MainEntity(temp, tempMin, tempMax, humidity)
fun Weather.asEntity(): WeatherEntity = WeatherEntity(main, description, icon)
fun Wind.asEntity(): WindEntity = WindEntity(speed, gust)
fun WeatherData.asEntity(zipcode: String): WeatherDataEntity =
    WeatherDataEntity(zipcode, dtTxt, main.asEntity(), weather.asEntity(), wind.asEntity())

//Data must already be in order by time stamp. This simply arranges into days.
fun organizeWeatherDataByDay(weatherList: List<WeatherData>): List<List<WeatherData>> {
    val listOfDays = mutableListOf<List<WeatherData>>()
    var oneDayForecastList = mutableListOf<WeatherData>()
    var currentDay: Int = weatherList[0].dtTxt.dayOfYear
    for (weatherData in weatherList) {
        val dataDay = weatherData.dtTxt.dayOfYear
        if (dataDay != currentDay) {
            if (oneDayForecastList.isNotEmpty()) {
                listOfDays.add(oneDayForecastList)
                oneDayForecastList = mutableListOf<WeatherData>()
            }
            currentDay = dataDay
        }
        oneDayForecastList.add(weatherData)
    }
    listOfDays.add(oneDayForecastList)
    return listOfDays
}