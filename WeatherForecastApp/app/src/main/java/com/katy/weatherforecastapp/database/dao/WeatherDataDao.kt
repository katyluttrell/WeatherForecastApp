package com.katy.weatherforecastapp.database.dao

import androidx.room.*
import com.katy.weatherforecastapp.model.local.WeatherDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDataDao {
    @Query("SELECT * FROM weatherData")
    fun getWeatherData(): Flow<List<WeatherDataEntity>>

    @Query("SELECT * FROM weatherData WHERE zipcode = :zipcode")
    fun getWeatherData(zipcode:String): Flow<List<WeatherDataEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addWeatherData(weatherData: WeatherDataEntity)

    @Transaction
    @Query("DELETE FROM weatherData")
    fun deleteAll()

    @Query("DELETE FROM weatherData WHERE SUBSTR(dtTxt, 1, 10) NOT IN (:keepDays)")
    @Transaction
    fun deleteOldData(keepDays:Set<String>)
}