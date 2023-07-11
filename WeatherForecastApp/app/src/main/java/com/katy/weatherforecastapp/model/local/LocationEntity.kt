package com.katy.weatherforecastapp.model.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.katy.weatherforecastapp.model.Location
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "location")
data class LocationEntity(
    @PrimaryKey
    val zipcode: String,
    val locationName: String,
    val lat: String,
    val lon: String
) : Parcelable

fun LocationEntity.asExternalModel() = Location(
    zipcode, locationName, lat, lon
)