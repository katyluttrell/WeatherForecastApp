package com.katy.weatherforecastapp.network

import com.katy.weatherforecastapp.model.WeatherData

sealed class NetworkResult<T> {
    sealed class Success<T> : NetworkResult<T>() {
        data class LocationSuccess<Location>(val response: Location) : Success<Location>()
        data class WeatherDataSuccess<WeatherData>(val response: List<List<WeatherData>>) : Success<List<List<WeatherData>>>()
    }
    object BadRequest : NetworkResult<Nothing>()
    object NetworkError : NetworkResult<Nothing>()
}
