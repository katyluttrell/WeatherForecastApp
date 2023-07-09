package com.katy.weatherforecastapp.network

sealed class NetworkResult<T> {
    data class Success<Location>(val response: Location) : NetworkResult<Location>()
    object BadRequest : NetworkResult<Nothing>()
    object NetworkError : NetworkResult<Nothing>()
}
