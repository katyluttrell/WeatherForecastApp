package com.katy.weatherforecastapp.model

import com.squareup.moshi.Json

data class LatLonResponse (
    @field:Json(name = "name") val locationName: String,
    @field:Json(name = "lat") val lat: String,
    @field:Json(name = "lon") val lon: String)