package com.katy.weatherforecastapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.katy.weatherforecastapp.model.Location
import com.katy.weatherforecastapp.model.WeatherData

@Dao
interface WeatherDataDao {
    @Query("SELECT * FROM weatherData")
    fun getWeatherData(): List<WeatherData>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addWeatherData(weatherData: WeatherData)
}