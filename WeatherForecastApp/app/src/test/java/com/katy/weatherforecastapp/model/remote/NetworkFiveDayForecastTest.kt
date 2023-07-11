package com.katy.weatherforecastapp.model.remote

import com.katy.weatherforecastapp.testObjects.TestObjectFactory
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


internal class NetworkFiveDayForecastTest {
    private lateinit var testObjectFactory: TestObjectFactory

    @Before
    fun setup() {
        testObjectFactory = TestObjectFactory()
    }

    @Test
    fun testMainAsExternalModel() {
        Assert.assertEquals(
            testObjectFactory.makeMain(),
            testObjectFactory.makeNetworkMain().asExternalModel()
        )
    }

    @Test
    fun testWeatherAsExternalModel() {
        Assert.assertEquals(
            testObjectFactory.makeWeather(),
            testObjectFactory.makeNetworkWeather().asExternalModel()
        )
    }

    @Test
    fun testWindAsExternalModel() {
        Assert.assertEquals(
            testObjectFactory.makeWind(),
            testObjectFactory.makeNetworkWind().asExternalModel()
        )
    }

    @Test
    fun testWeatherDataAsExternalModel() {
        Assert.assertEquals(
            testObjectFactory.makeWeatherData(),
            testObjectFactory.makeNetworkWeatherData().asExternalModel()
        )
    }

    @Test
    fun testNetwork5DayForecastsOrganizedWeatherDataList() {
        val originalData = testObjectFactory.makeNetwork5DayForecastUTC()
        val expectedData = testObjectFactory.makeWeatherData5DayListInOrder()
        assertEquals(expectedData, originalData.asOrganizedWeatherDataList())
    }

    @Test
    fun testNetwork5DayForecastsOrganizedWeatherDataListEmptyList() {
        val originalData = NetworkFiveDayForecast(
            listOf<NetworkWeatherData>(),
            NetworkCity(0)
        )
        val expectedData = null
        assertEquals(expectedData, originalData.asOrganizedWeatherDataList())
    }

}