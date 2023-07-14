package com.katy.weatherforecastapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.katy.weatherforecastapp.database.cleaning.DatabaseCleaningRoutines
import com.katy.weatherforecastapp.di.IoDispatcher
import com.katy.weatherforecastapp.model.Location
import com.katy.weatherforecastapp.model.WeatherData
import com.katy.weatherforecastapp.repository.LocationDataErrorCallbacks
import com.katy.weatherforecastapp.repository.LocationRepository
import com.katy.weatherforecastapp.repository.WeatherDataErrorCallbacks
import com.katy.weatherforecastapp.repository.WeatherRepository
import com.katy.weatherforecastapp.ui.dialog.MainViewDialog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import org.jetbrains.annotations.VisibleForTesting
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository,
    private val databaseCleaningRoutines: DatabaseCleaningRoutines,
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val locationCallbacks = object : LocationDataErrorCallbacks {
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
    val weatherCallbacks = object : WeatherDataErrorCallbacks {
        override fun onNetworkError() {
            currentDialog.postValue(MainViewDialog.NetworkFetchError)
        }

        override fun onNoInternetNoData() {
            currentDialog.postValue(MainViewDialog.NoInternetNoData)
        }

    }

    internal val currentDialog: MutableLiveData<MainViewDialog?> by lazy {
        MutableLiveData<MainViewDialog?>()
    }

    internal val  location: MutableLiveData<Location> by lazy {
        MutableLiveData<Location>()
    }

    internal val zipcodeValidationError: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    internal val weatherDataList: MutableLiveData<List<List<WeatherData>>> by lazy {
        MutableLiveData<List<List<WeatherData>>>()
    }
    
    internal fun  startObservingLocationData(zipcode: String) {
        viewModelScope.launch(ioDispatcher) {
            locationRepository.getLocationFlow(zipcode, locationCallbacks)
                .collect {
                    location.postValue(it)
                    startObservingWeatherData(it)
                }
        }
    }


    internal fun startObservingWeatherData(loc: Location) {
        viewModelScope.launch(ioDispatcher) {
            weatherRepository.getFiveDayForecastListFlow(loc, weatherCallbacks)
                .collect {
                    weatherDataList.postValue(it)
                }
        }
    }

    internal fun cleanDatabase(){
        if(!databaseCleaningRoutines.hasCleaned) {
            viewModelScope.launch(ioDispatcher) {
                databaseCleaningRoutines.cleanDatabase()
            }
        }
    }


}