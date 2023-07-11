package com.katy.weatherforecastapp.repository

import android.content.Context
import com.katy.weatherforecastapp.database.dao.LocationDao
import com.katy.weatherforecastapp.di.IoDispatcher
import com.katy.weatherforecastapp.model.Location
import com.katy.weatherforecastapp.model.asEntity
import com.katy.weatherforecastapp.model.local.asExternalModel
import com.katy.weatherforecastapp.network.NetworkResult
import com.katy.weatherforecastapp.network.NetworkUtils
import com.katy.weatherforecastapp.network.OpenWeatherApi
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val locationDao: LocationDao,
    private val openWeatherApi: OpenWeatherApi,
    private val networkUtils: NetworkUtils,
    @ApplicationContext private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : LocationRepository {


    private suspend fun cacheLocation(location: Location) = withContext(ioDispatcher){
        locationDao.addLocation(location.asEntity())
    }

    override suspend fun getLocationFlow(
        zipcode: String,
        errorCallbacks: LocationDataErrorCallbacks
    ): Flow<Location?> {
        val flow = locationDao.getLocation(zipcode)
        val location = flow.first()
        if (location == null) {
            if (networkUtils.hasInternetAccess(context)) {
                fetchLocation(zipcode, errorCallbacks)
            } else {
                errorCallbacks.onNoInternetNoData()
            }
        }
        return flow.map { it?.asExternalModel() }.filterNotNull()
    }

    private suspend fun fetchLocation(zipcode: String, errorCallbacks: LocationDataErrorCallbacks) {
        withContext(ioDispatcher) {
            when (val result = openWeatherApi.getLocation(zipcode)) {
                is NetworkResult.Success -> {
                    cacheLocation(result.response as Location)
                    //TODO start weather request
                }
                is NetworkResult.BadRequest -> {
                    errorCallbacks.onInvalidZipcode()
                }
                is NetworkResult.NetworkError -> {
                    errorCallbacks.onNetworkError()
                }
            }
        }
    }
}