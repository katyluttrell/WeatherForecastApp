package com.katy.weatherforecastapp.ui.dialog

sealed class MainViewDialog{
    object ZipCodePrompt : MainViewDialog()
    object NoInternetOldData : MainViewDialog()
    object NoInternetNoData : MainViewDialog()
    object NoInternetLocationOnly : MainViewDialog()
    object NetworkFetchError : MainViewDialog()
    object NoLocationChange : MainViewDialog()
}