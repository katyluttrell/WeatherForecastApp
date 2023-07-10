package com.katy.weatherforecastapp.model.local

import com.katy.weatherforecastapp.testObjects.TestObjectFactory
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

internal class WeatherDataEntityTest{
    private lateinit var testObjectFactory: TestObjectFactory

    @Before
    fun setup() {
        testObjectFactory = TestObjectFactory()
    }
    @Test
    fun testMainAsExternalModel() {
        Assert.assertEquals(
            testObjectFactory.makeMain(),
            testObjectFactory.makeMainEntity().asExternalModel()
        )
    }

    @Test
    fun testWeatherAsExternalModel() {
        Assert.assertEquals(
            testObjectFactory.makeWeather(),
            testObjectFactory.makeWeatherEntity().asExternalModel()
        )
    }

    @Test
    fun testWindAsExternalModel() {
        Assert.assertEquals(
            testObjectFactory.makeWind(),
            testObjectFactory.makeWindEntity().asExternalModel()
        )
    }

    @Test
    fun testWeatherDataAsExternalModel() {
        Assert.assertEquals(
            testObjectFactory.makeWeatherData(),
            testObjectFactory.makeWeatherDataEntity().asExternalModel()
        )
    }

    @Test fun testSortWeatherDataEntitiesByTimeStamp(){
        val beforeList = testObjectFactory.makeWeatherDataEntityListOutOfOrder()
        val expectedList = testObjectFactory.makeWeatherDataListOrganizedByTimestamp()
        assertEquals(expectedList, sortWeatherDataEntitiesByTimeStamp(beforeList))
    }
}