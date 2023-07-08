package com.katy.weatherforecastapp.repository

import com.katy.weatherforecastapp.database.dao.LocationDao
import com.katy.weatherforecastapp.model.Location
import javax.inject.Inject

interface LocationRepository{

    suspend fun addLocation(location: Location)

    suspend fun getLocation(): Location?
}