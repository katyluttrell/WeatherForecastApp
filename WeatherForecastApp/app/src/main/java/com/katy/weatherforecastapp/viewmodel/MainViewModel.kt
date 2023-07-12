package com.katy.weatherforecastapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.katy.weatherforecastapp.model.Location
import com.katy.weatherforecastapp.model.WeatherData
import com.katy.weatherforecastapp.repository.LocationDataErrorCallbacks
import com.katy.weatherforecastapp.repository.LocationRepository
import com.katy.weatherforecastapp.repository.WeatherDataErrorCallbacks
import com.katy.weatherforecastapp.repository.WeatherRepository
import com.katy.weatherforecastapp.ui.dialog.MainViewDialog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository
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

    fun editLocation() {
        currentDialog.postValue(
            MainViewDialog.ZipCodePrompt
        )
    }


    fun startObservingLocationData(zipcode: String) {
        val callbacks = object : LocationDataErrorCallbacks {
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
                    startObservingWeatherData(it)
                }
        }
    }

    private fun startObservingWeatherData(loc: Location) {
        val callbacks = object : WeatherDataErrorCallbacks {
            override fun onNetworkError() {
                currentDialog.postValue(MainViewDialog.NetworkFetchError)
            }

            override fun onNoInternetNoData() {
                currentDialog.postValue(MainViewDialog.NoInternetNoData)
            }

        }

        viewModelScope.launch {
            weatherRepository.getFiveDayForecastListFlow(loc, callbacks)
                .collect {
                    weatherDataList.postValue(it)
                }
        }
    }


}