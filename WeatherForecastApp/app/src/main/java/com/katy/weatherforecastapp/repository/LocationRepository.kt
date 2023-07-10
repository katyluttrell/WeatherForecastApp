package com.katy.weatherforecastapp.repository

import com.katy.weatherforecastapp.model.Location
import com.katy.weatherforecastapp.model.local.LocationEntity
import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    suspend fun cacheLocation(location: Location)

    suspend fun getLocationFlow(zipcode: String, errorCallbacks: DataErrorCallbacks): Flow<Location>

}