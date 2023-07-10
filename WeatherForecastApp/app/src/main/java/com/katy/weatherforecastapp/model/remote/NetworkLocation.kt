package com.katy.weatherforecastapp.model.remote

import com.katy.weatherforecastapp.model.Location
import com.squareup.moshi.Json

data class NetworkLocation(
    @field:Json(name = "name") val locationName: String,
    @field:Json(name = "lat") val lat: String,
    @field:Json(name = "lon") val lon: String
)

fun NetworkLocation.asExternalModel(zipcode: String) = Location(
    zipcode, locationName, lat, lon
)