package com.katy.weatherforecastapp.model.local

import android.os.Parcelable
import androidx.room.*
import com.katy.weatherforecastapp.adapter.DateAdapter
import com.katy.weatherforecastapp.model.*
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime


@Entity(tableName = "weatherData", primaryKeys = ["zipcode", "dtTxt"])
@Parcelize
data class WeatherDataEntity(
    val zipcode: String,
    @TypeConverters(DateAdapter::class) val dtTxt: LocalDateTime,
    @Embedded val main: MainEntity,
    @Embedded val weather: WeatherEntity,
    @Embedded val wind: WindEntity,
) : Parcelable

@Parcelize
data class MainEntity(
    val temp: Double,
    val tempMin: Double,
    val tempMax: Double,
    val humidity: String,
) : Parcelable

@Parcelize
data class WeatherEntity(
    val main: String,
    val description: String,
    val icon: String
) : Parcelable

@Parcelize
data class WindEntity(
    val speed: Double,
    val gust: Double
) : Parcelable

fun MainEntity.asExternalModel(): Main = Main(temp, tempMin, tempMax, humidity)
fun WeatherEntity.asExternalModel(): Weather = Weather(main, description, icon)
fun WindEntity.asExternalModel(): Wind = Wind(speed, gust)
fun WeatherDataEntity.asExternalModel(): WeatherData = WeatherData(
    dtTxt,
    main.asExternalModel(),
    weather.asExternalModel(),
    wind.asExternalModel()
)

fun sortWeatherDataEntitiesByTimeStamp(weatherList: List<WeatherDataEntity>): List<WeatherData> {
    return weatherList.sortedBy { it.dtTxt }.map { it.asExternalModel() }
}