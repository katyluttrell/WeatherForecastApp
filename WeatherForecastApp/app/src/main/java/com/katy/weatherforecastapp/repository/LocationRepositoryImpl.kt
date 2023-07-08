package com.katy.weatherforecastapp.repository

import com.katy.weatherforecastapp.database.dao.LocationDao
import com.katy.weatherforecastapp.database.dao.WeatherDataDao
import com.katy.weatherforecastapp.model.Location
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(private val locationDao: LocationDao): LocationRepository  {

    override suspend fun addLocation(location: Location) = locationDao.addLocation(location)

    override suspend fun getLocation(): Location? = locationDao.getLocation()
}