package com.katy.weatherforecastapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.katy.weatherforecastapp.model.Location
import com.katy.weatherforecastapp.model.WeatherData
import com.katy.weatherforecastapp.network.OpenWeatherApi
import com.katy.weatherforecastapp.repository.DataErrorCallbacks
import com.katy.weatherforecastapp.repository.LocationRepository
import com.katy.weatherforecastapp.ui.dialog.MainViewDialog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val openWeatherApi: OpenWeatherApi
) : ViewModel() {

    val currentDialog: MutableLiveData<MainViewDialog?> by lazy {
        MutableLiveData<MainViewDialog?>()
    }

    val location: MutableLiveData<Location> by lazy {
        MutableLiveData<Location>()
    }

    val zipcodeValidationError: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val weatherDataList: MutableLiveData<List<List<WeatherData>>> by lazy {
        MutableLiveData<List<List<WeatherData>>>()
    }

    var hasInternet: Boolean? = null

    private suspend fun fetchFiveDayForecast(location: Location): Boolean {
        return withContext(Dispatchers.IO) {
            val receivedWeather = openWeatherApi.getFiveDayForecast(location.lat, location.lon)
            if (receivedWeather != null) {
                weatherDataList.postValue(receivedWeather)
                true
            } else {
                false
            }
        }
    }

    fun editLocation() {
        currentDialog.postValue(
            MainViewDialog.ZipCodePrompt
        )
    }


    fun startObservingLocationData(zipcode: String) {
        val callbacks = object : DataErrorCallbacks {
            override fun onInvalidZipcode() {
                zipcodeValidationError.postValue(true)
            }

            override fun onNetworkError() {
                currentDialog.postValue(MainViewDialog.NetworkFetchError)
            }

            override fun onNoInternetNoData() {
                currentDialog.postValue(MainViewDialog.NoInternetNoData)
            }

        }
        viewModelScope.launch {
            locationRepository.getLocationFlow(zipcode, callbacks)
                .collect {
                    location.postValue(it)
                }
        }
    }


}