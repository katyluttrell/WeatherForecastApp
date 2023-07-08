package com.katy.weatherforecastapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.katy.weatherforecastapp.model.Location
import com.katy.weatherforecastapp.model.WeatherData
import com.katy.weatherforecastapp.network.OpenWeatherApi
import com.katy.weatherforecastapp.repository.LocationRepository
import com.katy.weatherforecastapp.repository.WeatherRepository
import com.katy.weatherforecastapp.ui.dialog.MainViewDialog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationRepository: LocationRepository,
    private val openWeatherApi: OpenWeatherApi
    ): ViewModel() {

    val currentDialog: MutableLiveData<MainViewDialog?> by lazy{
        MutableLiveData<MainViewDialog?>()
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
            locationRepository.addLocation(location)
        }
    }
    private fun addWeatherDataToDatabase(data: List<List<WeatherData>>) {
        GlobalScope.launch(Dispatchers.IO) {
            weatherRepository.deleteAllWeatherData()
            weatherRepository.addFiveDayForecastList(data)
        }
    }

     private suspend fun fetchFiveDayForecast(location: Location):Boolean {
        return withContext(Dispatchers.IO){
            val receivedWeather = openWeatherApi.getFiveDayForecast(location.lat, location.lon)
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
            val list = weatherRepository.getFiveDayForecastList()
            if(!list.isNullOrEmpty()){
                weatherDataList.postValue(list)
                currentDialog.postValue(MainViewDialog.NoInternetOldData)
            }else {
                currentDialog.postValue(MainViewDialog.NoInternetOldData)
            }
        }
    }

    suspend fun fetchLocation(zipcode:String): Boolean {
        return withContext(Dispatchers.IO) {
            val receivedLocation = openWeatherApi.getLatLong(zipcode)
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
            val cachedLocation = locationRepository.getLocation()
            if(cachedLocation!= null){
                location.postValue(cachedLocation)
                true
            }else{
                false
            }
        }
    }

    fun editLocation() {
        currentDialog.postValue(
            if(hasInternet==true){
                MainViewDialog.ZipCodePrompt
            } else{
                MainViewDialog.NoLocationChange
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
                 currentDialog.postValue(MainViewDialog.ZipCodePrompt)
             }
         }
    }

}