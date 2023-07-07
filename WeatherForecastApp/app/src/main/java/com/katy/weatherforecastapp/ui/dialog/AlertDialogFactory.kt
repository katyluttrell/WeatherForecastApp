package com.katy.weatherforecastapp.ui.dialog

import android.app.Activity
import android.app.AlertDialog
import android.app.AlertDialog.Builder
import android.content.Context
import com.katy.weatherforecastapp.R

class AlertDialogFactory {

    fun createDialog(builder: Builder, dialogEvent: DialogEvent): AlertDialog{
        return when(dialogEvent){
            DialogEvent.NoInternetLocationOnly -> okButtonDecorator(noWeatherDataDecorator(builder)).create()
            DialogEvent.NoInternetNoData -> okButtonDecorator(noInternetNoDataDecorator(builder)).create()
            DialogEvent.NoInternetOldData -> okButtonDecorator(noInternetOldDataDecorator(builder)).create()
            DialogEvent.NoLocationChange -> okButtonDecorator(noLocationChangeDecorator(builder)).create()
            else -> okButtonDecorator(networkFetchErrorDecorator(builder)).create()
        }
    }

     private fun noInternetOldDataDecorator(builder: Builder): Builder {
         return builder
             .setTitle(R.string.no_internet)
             .setMessage(R.string.old_data_message)
     }

    private fun okButtonDecorator(builder: Builder): Builder{
        return builder.setNeutralButton(R.string.ok) { dialog, _ ->
            dialog.dismiss()
        }
    }

    private fun noInternetNoDataDecorator(builder: Builder): Builder {
        return builder
            .setTitle(R.string.no_internet_no_data)
            .setMessage(R.string.try_again_with_internet)
    }

    private fun noLocationChangeDecorator(builder: Builder): Builder {
       return builder
            .setTitle(R.string.no_internet)
            .setMessage(R.string.no_internet_location_change_message)
    }

    private fun noWeatherDataDecorator(builder: Builder): Builder {
        return builder
            .setTitle(R.string.no_weather_data)
            .setMessage(R.string.try_again_with_internet)
    }

    private fun networkFetchErrorDecorator(builder: Builder): Builder{
        return builder
            .setTitle(R.string.network_fetch_error)
            .setMessage(R.string.please_try_again_later)
    }
}

