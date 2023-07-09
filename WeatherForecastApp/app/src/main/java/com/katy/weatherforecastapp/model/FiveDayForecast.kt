package com.katy.weatherforecastapp.model

import android.os.Parcelable
import androidx.room.*
import com.katy.weatherforecastapp.adapter.DateAdapter
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

data class FiveDayForecast(
    @field:Json(name = "list") val list: List<WeatherData>,
    @field:Json(name = "city") val city: City,
)

@Entity(tableName = "weatherData")
@Parcelize
data class WeatherData(
    @PrimaryKey @TypeConverters(DateAdapter::class) @field:Json(name = "dt_txt") var dtTxt: LocalDateTime,
    @Embedded @field:Json(name = "main") val main: Main,
    @Embedded @field:Json(name = "weather") val weather: Weather,
    @Embedded @field:Json(name = "wind") val wind: Wind,
) : Parcelable

@Parcelize
data class Main(
    @field:Json(name = "temp") val temp: Double,
    @field:Json(name = "temp_min") val tempMin: Double,
    @field:Json(name = "temp_max") val tempMax: Double,
    @field:Json(name = "humidity") val humidity: String,
) : Parcelable

@Parcelize
data class Weather(
    @field:Json(name = "main") val main: String,
    @field:Json(name = "description") val description: String,
    @field:Json(name = "icon") val icon: String
) : Parcelable

@Parcelize
data class Wind(
    @field:Json(name = "speed") val speed: Double, @field:Json(name = "gust") val gust: Double
) : Parcelable

data class City(
    @field:Json(name = "timezone") val timezone: Int
)
