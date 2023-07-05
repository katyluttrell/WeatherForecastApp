package com.katy.weatherforecastapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "location", primaryKeys = ["lat", "lon"])
data class Location (
    @field:Json(name = "name") val locationName: String,
    @field:Json(name = "lat") val lat: String,
    @field:Json(name = "lon") val lon: String
    ):Parcelable