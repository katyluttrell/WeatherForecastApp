package com.katy.weatherforecastapp.database

import android.content.Context
import androidx.room.*
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.katy.weatherforecastapp.database.dao.WeatherDataDao
import com.katy.weatherforecastapp.model.WeatherData
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class WeatherDatabaseTestWeatherDataTable {
    private lateinit var weatherDataDao: WeatherDataDao
    private lateinit var database: WeatherDatabase


    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context, WeatherDatabase::class.java
        ).build()
        weatherDataDao = database.weatherDataDao()
    }

    @After
    @Throws(IOException::class)
    fun teardown() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun testReadWriteData() {
        val weatherData = WeatherDataFactory().makeWeatherData()
        weatherDataDao.addWeatherData(weatherData)
        var retrievedData: List<WeatherData>
        runBlocking { retrievedData = weatherDataDao.getWeatherData() }
        assertEquals(weatherData, retrievedData[0])
    }

    @Test
    @Throws(Exception::class)
    fun testReadDataEmptyTable() {
        var retrievedData: List<WeatherData>
        runBlocking { retrievedData = weatherDataDao.getWeatherData() }
        assertEquals(emptyList<WeatherData>(), retrievedData)
    }


    @Test
    @Throws(Exception::class)
    fun testConflictStrategyReplace() {
        val weatherDataFactory = WeatherDataFactory()
        val data = weatherDataFactory.makeWeatherData(1, 1, 1, 1)
        val conflictData = weatherDataFactory.makeWeatherData(1, 2, 2, 2)
        weatherDataDao.addWeatherData(data)
        weatherDataDao.addWeatherData(conflictData)
        var retrievedData: List<WeatherData>
        runBlocking { retrievedData = weatherDataDao.getWeatherData() }
        assertEquals(1, retrievedData.size)
        assertEquals(conflictData, retrievedData[0])
    }
}