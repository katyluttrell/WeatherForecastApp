package com.katy.weatherforecastapp.model

import com.katy.weatherforecastapp.model.local.LocationEntity


data class Location(
    val zipcode: String, val locationName: String, val lat: String, val lon: String
)

fun Location.asEntity() = LocationEntity(
    zipcode, locationName, lat, lon
)