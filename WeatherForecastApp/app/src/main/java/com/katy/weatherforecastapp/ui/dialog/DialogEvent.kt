package com.katy.weatherforecastapp.ui.dialog

sealed class DialogEvent{
    object ZipCodePrompt : DialogEvent()
    object NoInternetOldData : DialogEvent()
    object NoInternetNoData : DialogEvent()
    object NoInternetLocationOnly : DialogEvent()
    object NetworkFetchError : DialogEvent()
    object NoLocationChange : DialogEvent()
}