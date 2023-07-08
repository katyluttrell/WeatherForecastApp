package com.katy.weatherforecastapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.katy.weatherforecastapp.App
import com.katy.weatherforecastapp.model.Location
import com.katy.weatherforecastapp.model.WeatherData
import com.katy.weatherforecastapp.ui.dialog.DialogEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {
    private val repository by lazy { App.repository }

    val dialogEvent: MutableLiveData<DialogEvent?> by lazy{
        MutableLiveData<DialogEvent?>()
    }

    val location: MutableLiveData<Location> by lazy {
        MutableLiveData<Location>()
    }

    val weatherDataList: MutableLiveData<List<List<WeatherData>>> by lazy {
        MutableLiveData<List<List<WeatherData>>>()
    }

    var hasInternet: Boolean? = null
     private fun addLocationToDatabase(location:Location) {
        GlobalScope.launch(Dispatchers.IO) {
            repository.addLocation(location)
        }
    }
    private fun addWeatherDataToDatabase(data: List<List<WeatherData>>) {
        GlobalScope.launch(Dispatchers.IO) {
            repository.deleteAllWeatherData()
            repository.addFiveDayForecastList(data)
        }
    }

     private suspend fun fetchFiveDayForecast(location: Location):Boolean {
        return withContext(Dispatchers.IO){
            val receivedWeather = App.openWeatherApi.getFiveDayForecast(location.lat, location.lon)
            if(receivedWeather != null){
                weatherDataList.postValue(receivedWeather)
                addWeatherDataToDatabase(receivedWeather)
                true
             }else{
                false
            }
        }
    }
      private suspend fun checkForCachedWeatherData() {
        return withContext(Dispatchers.IO) {
            val list = repository.getFiveDayForecastList()
            if(!list.isNullOrEmpty()){
                weatherDataList.postValue(list)
                dialogEvent.postValue(DialogEvent.NoInternetOldData)
            }else {
                dialogEvent.postValue(DialogEvent.NoInternetOldData)
            }
        }
    }

    suspend fun fetchLocation(zipcode:String): Boolean {
        return withContext(Dispatchers.IO) {
            val receivedLocation = App.openWeatherApi.getLatLong(zipcode)
            if (receivedLocation != null) {
                location.postValue(receivedLocation)
                addLocationToDatabase(receivedLocation)
                fetchFiveDayForecast(receivedLocation)
                true
            } else {
                false
            }
        }
    }

    suspend fun checkForCachedLocation():Boolean {
        return withContext(Dispatchers.IO) {
            val cachedLocation = repository.getLocation()
            if(cachedLocation!= null){
                location.postValue(cachedLocation)
                true
            }else{
                false
            }
        }
    }

    fun editLocation() {
        dialogEvent.postValue(
            if(hasInternet==true){
                DialogEvent.ZipCodePrompt
            } else{
                DialogEvent.NoLocationChange
            }
        )
    }

     fun startNetworkOrCacheFetches() {
         if(!location.isInitialized){
             if(hasInternet != true){
                 GlobalScope.launch(Dispatchers.IO) {
                     if (checkForCachedLocation()) {
                         checkForCachedWeatherData()
                     }
                 }
             } else {
                 dialogEvent.postValue(DialogEvent.ZipCodePrompt)
             }
         }
    }

}