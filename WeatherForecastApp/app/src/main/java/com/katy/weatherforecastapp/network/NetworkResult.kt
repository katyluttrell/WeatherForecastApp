package com.katy.weatherforecastapp.network

import com.katy.weatherforecastapp.model.WeatherData

sealed class NetworkResult<T> {
    data class Success<T>(val response: T) : NetworkResult<T>()
    object BadRequest : NetworkResult<Nothing>()
    object NetworkError : NetworkResult<Nothing>()
}
