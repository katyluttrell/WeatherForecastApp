package com.katy.weatherforecastapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.katy.weatherforecastapp.model.Location

@Dao
interface LocationDao {
    @Query("SELECT * FROM location")
    fun getLocation(): Location?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addLocation(location: Location)
}