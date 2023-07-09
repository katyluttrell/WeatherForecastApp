package com.katy.weatherforecastapp.repository

interface DataErrorCallbacks{
    fun onInvalidZipcode()
    fun onNetworkError()
    fun onNoInternetNoData()
}