package com.katy.weatherforecastapp.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.katy.weatherforecastapp.database.dao.LocationDao
import com.katy.weatherforecastapp.model.local.LocationEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class WeatherDatabaseTestLocationTable {
    private lateinit var locationDao: LocationDao
    private lateinit var database: WeatherDatabase
    private val location = LocationEntity(
        "80303",
        "Boulder",
        "39.9914",
        "-105.239"
    )

    @Before
    fun setup(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context, WeatherDatabase::class.java).build()
        locationDao= database.locationDao()
    }
    @After
    @Throws(IOException::class)
    fun teardown() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun testReadWriteDataLocation() {
        locationDao.addLocation(location)
        var retrievedLocation: LocationEntity?
        runBlocking {retrievedLocation = locationDao.getLocation("80303").first()}
        assert(retrievedLocation == location)
    }
    @Test
    @Throws(Exception::class)
    fun testReadDataLocationNotInTable() {
        locationDao.addLocation(location)
        var retrievedLocation: LocationEntity?
        runBlocking {retrievedLocation = locationDao.getLocation("80301").first()}
        assert(retrievedLocation == null)
    }
    @Test
    @Throws(Exception::class)
    fun testReadDataEmptyTable() {
        var retrievedLocation: LocationEntity?
        runBlocking {retrievedLocation = locationDao.getLocation("80303").first()}
        assert(retrievedLocation == null)
    }

    @Test
    @Throws(Exception::class)
    fun testConflictStrategyReplace() {
        locationDao.addLocation(location)
        val conflictLocation = LocationEntity(
            "80303",
            "Boulder 2.0 with a new name",
            "39.9914",
            "-105.239"
        )
        locationDao.addLocation(conflictLocation)
        var retrievedLocation: LocationEntity?
        runBlocking {retrievedLocation = locationDao.getLocation("80303").first()}
        assert(retrievedLocation == conflictLocation)
    }
}