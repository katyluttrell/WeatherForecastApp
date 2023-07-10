package com.katy.weatherforecastapp.model.remote

import androidx.annotation.VisibleForTesting
import com.katy.weatherforecastapp.model.*
import com.squareup.moshi.Json
import java.time.LocalDateTime

data class NetworkFiveDayForecast(
    @field:Json(name = "list") val list: List<NetworkWeatherData>,
    @field:Json(name = "city") val city: NetworkCity,
)

data class NetworkWeatherData(
    @field:Json(name = "dt_txt") val dtTxt: LocalDateTime,
    @field:Json(name = "main") val main: NetworkMain,
    @field:Json(name = "weather") val weather: NetworkWeather,
    @field:Json(name = "wind") val wind: NetworkWind,
)

data class NetworkMain(
    @field:Json(name = "temp") val temp: Double,
    @field:Json(name = "temp_min") val tempMin: Double,
    @field:Json(name = "temp_max") val tempMax: Double,
    @field:Json(name = "humidity") val humidity: String,
)

data class NetworkWeather(
    @field:Json(name = "main") val main: String,
    @field:Json(name = "description") val description: String,
    @field:Json(name = "icon") val icon: String
)

data class NetworkWind(
    @field:Json(name = "speed") val speed: Double,
    @field:Json(name = "gust") val gust: Double
)

data class NetworkCity(
    @field:Json(name = "timezone") val timezone: Int
)

fun NetworkWeatherData.asExternalModel(): WeatherData = WeatherData(
    dtTxt,
    main.asExternalModel(),
    weather.asExternalModel(),
    wind.asExternalModel()
)

fun NetworkMain.asExternalModel(): Main = Main(temp, tempMin, tempMax, humidity )
fun NetworkWeather.asExternalModel(): Weather = Weather(main, description, icon)
fun NetworkWind.asExternalModel(): Wind = Wind(speed, gust)

fun NetworkFiveDayForecast.asOrganizedWeatherDataList(): List<List<WeatherData>>{
    val timezoneResolvedList = resolveTimeZone(list, city.timezone)
    return organizeWeatherDataByDay(timezoneResolvedList)
}


private fun resolveTimeZone(list: List<NetworkWeatherData>, offset:Int): List<WeatherData> =
    list.map {  WeatherData(
        convertToLocalTime(it.dtTxt, offset),
        it.main.asExternalModel(),
        it.weather.asExternalModel(),
        it.wind.asExternalModel())}


private fun convertToLocalTime(date: LocalDateTime, shift: Int): LocalDateTime {
    return date.plusSeconds(shift.toLong())
}