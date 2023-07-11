package com.katy.weatherforecastapp.repository

import com.katy.weatherforecastapp.model.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    suspend fun getLocationFlow(
        zipcode: String,
        errorCallbacks: LocationDataErrorCallbacks
    ): Flow<Location>
}