package com.katy.weatherforecastapp.repository

import com.katy.weatherforecastapp.database.dao.LocationDao
import com.katy.weatherforecastapp.model.Location

class WeatherRepositoryImpl (private val locationDao: LocationDao): WeatherRepository{

    override suspend fun addLocation(location: Location) = locationDao.addLocation(location)

    override suspend fun getLocation(): Location? = locationDao.getLocation()

}