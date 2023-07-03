package com.katy.weatherforecastapp.model
import com.squareup.moshi.Json
import java.time.LocalDateTime

data class FiveDayForecast(
    @field:Json(name = "list") val list: List<List<WeatherData>>,
)

data class WeatherData(
    @field:Json(name = "main") val main: Main,
    @field:Json(name = "weather") val weather: Weather,
    @field:Json(name = "wind") val wind: Wind,
    @field:Json(name = "dt_txt") val dtTxt: LocalDateTime
)

data class Main(
    @field:Json(name = "temp") val temp: Double,
    @field:Json(name = "feels_like") val feelsLike: Double,
    @field:Json(name = "temp_min") val tempMin: Double,
    @field:Json(name = "temp_max") val tempMax: Double,
    @field:Json(name = "pressure") val pressure: Int,
    @field:Json(name = "sea_level") val seaLevel: Int,
    @field:Json(name = "grnd_level") val grndLevel: Int,
    @field:Json(name = "humidity") val humidity: Int,
    @field:Json(name = "temp_kf") val tempKf: Double
)

data class Weather(
    @field:Json(name = "main") val main: String,
    @field:Json(name = "description") val description: String,
    @field:Json(name = "icon") val icon: String
)
data class Wind(
    @field:Json(name = "speed") val speed: Double,
    @field:Json(name = "deg") val deg: Int,
    @field:Json(name = "gust") val gust: Double
)