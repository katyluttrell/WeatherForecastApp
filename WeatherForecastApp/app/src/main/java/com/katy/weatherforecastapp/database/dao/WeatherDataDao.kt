package com.katy.weatherforecastapp.database.dao

import androidx.room.*
import com.katy.weatherforecastapp.model.WeatherData
import com.katy.weatherforecastapp.model.local.WeatherDataEntity

@Dao
interface WeatherDataDao {
    @Query("SELECT * FROM weatherData")
    fun getWeatherData(): List<WeatherDataEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addWeatherData(weatherData: WeatherDataEntity)

    @Transaction
    @Query("DELETE FROM weatherData")
    fun deleteAll()
}