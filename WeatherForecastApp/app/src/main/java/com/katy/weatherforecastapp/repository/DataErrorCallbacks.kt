package com.katy.weatherforecastapp.repository
interface DataErrorCallbacks {
    fun onNetworkError()
    fun onNoInternetNoData()
}
interface LocationDataErrorCallbacks : DataErrorCallbacks {
    fun onInvalidZipcode()
}
interface WeatherDataErrorCallbacks : DataErrorCallbacks {}