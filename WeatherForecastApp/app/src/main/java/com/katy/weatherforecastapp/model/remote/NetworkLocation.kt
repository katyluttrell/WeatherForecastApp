package com.katy.weatherforecastapp.model.remote

import com.katy.weatherforecastapp.model.local.LocationEntity
import com.squareup.moshi.Json

data class NetworkLocation(
    @field:Json(name = "name") val locationName: String,
    @field:Json(name = "lat") val lat: String,
    @field:Json(name = "lon") val lon: String
)

fun NetworkLocation.asEntity(zipcode: String) = LocationEntity(
    zipcode, locationName, lat, lon
)