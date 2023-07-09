package com.katy.weatherforecastapp.database

import com.katy.weatherforecastapp.model.Main
import com.katy.weatherforecastapp.model.Weather
import com.katy.weatherforecastapp.model.WeatherData
import com.katy.weatherforecastapp.model.Wind
import java.time.LocalDateTime
import java.time.Month

class WeatherDataFactory {

    fun makeDate(version: Int = 1): LocalDateTime {
        return when (version) {
            2 -> LocalDateTime.of(2023, Month.JULY, 8, 9, 0, 0)
            else -> LocalDateTime.of(2023, Month.JULY, 9, 12, 0, 0)
        }
    }

    fun makeWind(version: Int = 1): Wind {
        return when (version) {
            2 -> Wind(
                9.2,
                40.8
            )
            else -> Wind(
                5.3,
                15.0
            )
        }
    }

    fun makeWeather(version: Int = 1): Weather {
        return when (version) {
            2 -> Weather(
                "cloudy",
                "partly cloudy",
                "03n"
            )
            else -> Weather(
                "rain",
                "super heavy rain",
                "11d"
            )
        }
    }

    fun makeMain(version: Int = 1): Main {
        return when (version) {
            2 -> Main(
                92.5,
                80.3,
                109.7,
                "0.6"
            )
            else -> Main(
                72.5,
                60.1,
                100.4,
                "0.2"
            )
        }
    }


    fun makeWeatherData(
        dateVersion: Int = 1,
        mainVersion: Int = 1,
        weatherVersion: Int = 1,
        windVersion: Int = 1,
    ): WeatherData =
        WeatherData(
            makeDate(dateVersion),
            makeMain(mainVersion),
            makeWeather(weatherVersion),
            makeWind(windVersion)
        )
}