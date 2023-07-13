package com.katy.weatherforecastapp.testObjects

import com.katy.weatherforecastapp.model.*
import com.katy.weatherforecastapp.model.local.*
import com.katy.weatherforecastapp.model.remote.*
import java.time.LocalDateTime
import java.time.Month

class TestObjectFactory {

    fun makeZipcode(version: Int = 1): String {
        return when (version) {
            else -> "80303"
        }
    }

    fun makeLocationObject(version: Int = 1): Location {
        return when (version) {
            else -> Location(
                makeZipcode(version),
                "Boulder",
                "39.9914",
                "-105.239"
            )
        }
    }

    fun makeLocationEntityObject(version: Int = 1): LocationEntity {
        return when (version) {
            else -> LocationEntity(
                makeZipcode(version),
                "Boulder",
                "39.9914",
                "-105.239"
            )
        }
    }

    fun makeNetworkLocationObject(version: Int = 1): NetworkLocation {
        return when (version) {
            else -> NetworkLocation(
                "Boulder",
                "39.9914",
                "-105.239"
            )
        }
    }

    fun makeDateTimeUTC(version: Int = 1): LocalDateTime = makeDateTime(version).plusHours(6)


    fun makeDateTime(version: Int = 1): LocalDateTime {
        return when (version) {
            1 -> LocalDateTime.of(2023, Month.JULY, 9, 18, 0, 0)
            2 -> LocalDateTime.of(2023, Month.JULY, 9, 21, 0, 0)
            3 -> LocalDateTime.of(2023, Month.JULY, 10, 0, 0, 0)
            4 -> LocalDateTime.of(2023, Month.JULY, 10, 3, 0, 0)
            5 -> LocalDateTime.of(2023, Month.JULY, 10, 9, 0, 0)
            6 -> LocalDateTime.of(2023, Month.JULY, 10, 12, 0, 0)
            7 -> LocalDateTime.of(2023, Month.JULY, 10, 15, 0, 0)
            8 -> LocalDateTime.of(2023, Month.JULY, 10, 18, 0, 0)
            9 -> LocalDateTime.of(2023, Month.JULY, 10, 21, 0, 0)
            10 -> LocalDateTime.of(2023, Month.JULY, 11, 0, 0, 0)
            11 -> LocalDateTime.of(2023, Month.JULY, 11, 3, 0, 0)
            12 -> LocalDateTime.of(2023, Month.JULY, 11, 9, 0, 0)
            13 -> LocalDateTime.of(2023, Month.JULY, 11, 12, 0, 0)
            14 -> LocalDateTime.of(2023, Month.JULY, 11, 15, 0, 0)
            15 -> LocalDateTime.of(2023, Month.JULY, 11, 18, 0, 0)
            16 -> LocalDateTime.of(2023, Month.JULY, 11, 21, 0, 0)
            17 -> LocalDateTime.of(2023, Month.JULY, 12, 0, 0, 0)
            18 -> LocalDateTime.of(2023, Month.JULY, 12, 3, 0, 0)
            19 -> LocalDateTime.of(2023, Month.JULY, 12, 9, 0, 0)
            20 -> LocalDateTime.of(2023, Month.JULY, 12, 12, 0, 0)
            21 -> LocalDateTime.of(2023, Month.JULY, 12, 15, 0, 0)
            22 -> LocalDateTime.of(2023, Month.JULY, 12, 18, 0, 0)
            23 -> LocalDateTime.of(2023, Month.JULY, 12, 21, 0, 0)
            24 -> LocalDateTime.of(2023, Month.JULY, 13, 0, 0, 0)
            25 -> LocalDateTime.of(2023, Month.JULY, 13, 3, 0, 0)
            26 -> LocalDateTime.of(2023, Month.JULY, 13, 6, 0, 0)
            27 -> LocalDateTime.of(2023, Month.JULY, 13, 9, 0, 0)
            28 -> LocalDateTime.of(2023, Month.JULY, 13, 12, 0, 0)
            29 -> LocalDateTime.of(2023, Month.JULY, 13, 15, 0, 0)
            30 -> LocalDateTime.of(2023, Month.JULY, 13, 18, 0, 0)
            else -> LocalDateTime.of(2023, Month.JULY, 13, 21, 0, 0)
        }
    }


    fun makeWind(version: Int = 1): Wind {
        return when (version) {
            1 -> Wind(
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
            1 -> Weather(
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
            1 -> Main(
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
            makeDateTime(dateVersion),
            makeMain(mainVersion),
            makeWeather(weatherVersion),
            makeWind(windVersion)
        )

    fun makeWindEntity(version: Int = 1): WindEntity {
        return when (version) {
            1 -> WindEntity(
                9.2,
                40.8
            )
            else -> WindEntity(
                5.3,
                15.0
            )
        }
    }

    fun makeWeatherEntity(version: Int = 1): WeatherEntity {
        return when (version) {
            1 -> WeatherEntity(
                "cloudy",
                "partly cloudy",
                "03n"
            )
            else -> WeatherEntity(
                "rain",
                "super heavy rain",
                "11d"
            )
        }
    }

    fun makeMainEntity(version: Int = 1): MainEntity {
        return when (version) {
            1 -> MainEntity(
                92.5,
                80.3,
                109.7,
                "0.6"
            )
            else -> MainEntity(
                72.5,
                60.1,
                100.4,
                "0.2"
            )
        }
    }

    fun makeWeatherDataEntity(
        zipcodeVersion: Int = 1,
        dateVersion: Int = 1,
        mainVersion: Int = 1,
        weatherVersion: Int = 1,
        windVersion: Int = 1,
    ): WeatherDataEntity =
        WeatherDataEntity(
            makeZipcode(zipcodeVersion),
            makeDateTime(dateVersion),
            makeMainEntity(mainVersion),
            makeWeatherEntity(weatherVersion),
            makeWindEntity(windVersion)
        )

    fun makeNetworkWind(version: Int = 1): NetworkWind {
        return when (version) {
            1 -> NetworkWind(
                9.2,
                40.8
            )
            else -> NetworkWind(
                5.3,
                15.0
            )
        }
    }

    fun makeNetworkWeather(version: Int = 1): NetworkWeather {
        return when (version) {
            1 -> NetworkWeather(
                "cloudy",
                "partly cloudy",
                "03n"
            )
            else -> NetworkWeather(
                "rain",
                "super heavy rain",
                "11d"
            )
        }
    }

    fun makeNetworkMain(version: Int = 1): NetworkMain {
        return when (version) {
            1 -> NetworkMain(
                92.5,
                80.3,
                109.7,
                "0.6"
            )
            else -> NetworkMain(
                72.5,
                60.1,
                100.4,
                "0.2"
            )
        }
    }

    fun makeNetworkCity(version: Int = 1): NetworkCity {
        return when (version) {
            else -> NetworkCity(
                -21600
            )
        }
    }

    fun makeNetwork5DayForecastUTC(version: Int = 1): NetworkFiveDayForecast {
        return when (version) {
            else -> NetworkFiveDayForecast(
                makeNetworkWeatherDataListOrganizedByTimestampUTC(),
                makeNetworkCity()
            )
        }
    }

    fun makeNetworkWeatherData(
        dateVersion: Int = 1,
        mainVersion: Int = 1,
        weatherVersion: Int = 1,
        windVersion: Int = 1,
    ): NetworkWeatherData =
        NetworkWeatherData(
            makeDateTime(dateVersion),
            makeNetworkMain(mainVersion),
            makeNetworkWeather(weatherVersion),
            makeNetworkWind(windVersion)
        )

    fun makeNetworkWeatherDataUTC(
        dateVersion: Int = 1,
        mainVersion: Int = 1,
        weatherVersion: Int = 1,
        windVersion: Int = 1,
    ): NetworkWeatherData =
        NetworkWeatherData(
            makeDateTimeUTC(dateVersion),
            makeNetworkMain(mainVersion),
            makeNetworkWeather(weatherVersion),
            makeNetworkWind(windVersion)
        )


    fun makeWeatherDataEntityListOutOfOrder(): List<WeatherDataEntity> {
        val order = listOf(
            21,
            9,
            14,
            30,
            6,
            3,
            12,
            18,
            1,
            29,
            24,
            7,
            15,
            25,
            27,
            2,
            11,
            8,
            17,
            10,
            5,
            19,
            23,
            13,
            22,
            4,
            28,
            20,
            16,
            26
        )
        val finalList = mutableListOf<WeatherDataEntity>()
        for (i in order) {
            finalList.add(makeWeatherDataEntity(dateVersion = i))
        }
        return finalList
    }

    fun makeNetworkWeatherDataListOrganizedByTimestampUTC(): List<NetworkWeatherData> {
        val finalList = mutableListOf<NetworkWeatherData>()
        for (i in 1..30) {
            finalList.add(makeNetworkWeatherDataUTC(i))
        }
        return finalList
    }

    fun makeWeatherDataListOrganizedByTimestamp(): List<WeatherData> {
        val finalList = mutableListOf<WeatherData>()
        for (i in 1..30) {
            finalList.add(makeWeatherData(i))
        }
        return finalList
    }

    fun makeWeatherData5DayListInOrder(): List<List<WeatherData>> {
        val finalList = mutableListOf<List<WeatherData>>()
        val day1 = listOf(makeWeatherData(1), makeWeatherData(2))
        finalList.add(day1)
        var day = mutableListOf<WeatherData>()
        for (i in 3..30) {
            day.add(makeWeatherData(i))
            if ((i - 2) % 7 == 0) {
                finalList.add(day)
                day = mutableListOf()
            }
        }
        return finalList
    }

    fun makeWeatherDataEntity5DayListInOrder(): List<List<WeatherDataEntity>> {
        val finalList = mutableListOf<List<WeatherDataEntity>>()
        val day1 = listOf(makeWeatherDataEntity(1), makeWeatherDataEntity(2))
        finalList.add(day1)
        var day = mutableListOf<WeatherDataEntity>()
        for (i in 3..30) {
            day.add(makeWeatherDataEntity(i))
            if ((i - 2) % 7 == 0) {
                finalList.add(day)
                day = mutableListOf()
            }
        }
        return finalList
    }

    fun makeOneDayOfWeatherData(): List<WeatherData> {
        val day = mutableListOf<WeatherData>()
        for (i in 24..31) {
            day.add(makeWeatherData(i))
        }
        return day
    }
}