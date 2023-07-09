package com.katy.weatherforecastapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.katy.weatherforecastapp.model.local.LocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Query("SELECT * FROM location limit 1")
    fun getLocation(): LocationEntity?

    @Query("SELECT * FROM location WHERE zipcode = :zipcode")
    fun getLocation(zipcode: String): Flow<LocationEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addLocation(location: LocationEntity)
}