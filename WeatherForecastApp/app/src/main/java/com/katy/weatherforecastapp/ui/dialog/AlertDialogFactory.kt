package com.katy.weatherforecastapp.ui.dialog

import android.app.AlertDialog
import android.app.AlertDialog.Builder
import com.katy.weatherforecastapp.R

class AlertDialogFactory {
    fun createDialog(builder: Builder, mainViewDialog: MainViewDialog, onOkCallback: OnOkCallback?): AlertDialog =
        okButtonDecorator(titleAndMessageDecorator(builder, mainViewDialog), onOkCallback).create()


    private fun titleAndMessageDecorator(builder: Builder, mainViewDialog: MainViewDialog): Builder{
         return when(mainViewDialog){
            MainViewDialog.NoInternetLocationOnly -> noWeatherDataDecorator(builder)
            MainViewDialog.NoInternetNoData -> noInternetNoDataDecorator(builder)
            MainViewDialog.NoInternetOldData ->noInternetOldDataDecorator(builder)
            MainViewDialog.NoLocationChange -> noLocationChangeDecorator(builder)
            else -> networkFetchErrorDecorator(builder)
        }
    }


     private fun noInternetOldDataDecorator(builder: Builder): Builder {
         return builder
             .setTitle(R.string.no_internet)
             .setMessage(R.string.old_data_message)
     }

    private fun okButtonDecorator(builder: Builder, onOkCallback: OnOkCallback?): Builder{
        return builder.setNeutralButton(R.string.ok) { dialog, _ ->
            dialog.dismiss()
            onOkCallback?.onOkPress()
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
interface OnOkCallback{
    fun onOkPress()
}
