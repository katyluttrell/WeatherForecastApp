package com.katy.weatherforecastapp.database.dao

import androidx.room.*
import com.katy.weatherforecastapp.model.WeatherData

@Dao
interface WeatherDataDao {
    @Query("SELECT * FROM weatherData")
    fun getWeatherData(): List<WeatherData>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addWeatherData(weatherData: WeatherData)

    @Transaction
    @Query("DELETE FROM weatherData")
    fun deleteAll()
}