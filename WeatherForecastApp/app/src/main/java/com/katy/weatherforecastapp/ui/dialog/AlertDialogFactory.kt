package com.katy.weatherforecastapp.ui.dialog

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import com.katy.weatherforecastapp.R

class AlertDialogFactory {

     fun createNoInternetOldDataDialog(context: Context): AlertDialog {
        return AlertDialog.Builder(context)
             .setTitle(R.string.no_internet)
             .setMessage(R.string.old_data_message)
             .setNeutralButton(R.string.ok) { dialog, _ ->
                 dialog.dismiss()
             }
             .create()
     }

    fun createNoInternetNoDataDialog(context: Context): AlertDialog {
        return AlertDialog.Builder(context)
            .setTitle(R.string.no_internet_no_data)
            .setMessage(R.string.try_again_with_internet)
            .setNeutralButton(R.string.ok) { dialog, _ ->
                    dialog.dismiss()
            }
            .create()
    }

    fun createNoLocationChangeDialog(context: Context): AlertDialog {
       return AlertDialog.Builder(context)
            .setTitle(R.string.no_internet)
            .setMessage(R.string.no_internet_location_change_message)
            .setNeutralButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }

    fun createNoWeatherDataDialog(context: Context): AlertDialog {
        return AlertDialog.Builder(context)
            .setTitle(R.string.no_weather_data)
            .setMessage(R.string.try_again_with_internet)
            .setNeutralButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }
}

