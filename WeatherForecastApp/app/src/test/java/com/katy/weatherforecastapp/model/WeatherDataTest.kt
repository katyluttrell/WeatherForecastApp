package com.katy.weatherforecastapp.model

import com.katy.weatherforecastapp.util.testUtil.testObjects.TestObjectFactory
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


internal class WeatherDataTest {

    private lateinit var testObjectFactory: TestObjectFactory
    @Before
    fun setup() {
        testObjectFactory = TestObjectFactory()
    }

    @Test
    fun testMainAsEntity() {
        assertEquals(testObjectFactory.makeMainEntity(), testObjectFactory.makeMain().asEntity())
    }

    @Test
    fun testWeatherAsEntity() {
        assertEquals(
            testObjectFactory.makeWeatherEntity(),
            testObjectFactory.makeWeather().asEntity()
        )
    }

    @Test
    fun testWindAsEntity() {
        assertEquals(testObjectFactory.makeWindEntity(), testObjectFactory.makeWind().asEntity())
    }

    @Test
    fun testWeatherDataAsEntity() {
        assertEquals(
            testObjectFactory.makeWeatherDataEntity(),
            testObjectFactory.makeWeatherData().asEntity("80303")
        )
    }

    @Test
    fun testOrganizeWeatherDataByDay() {
        val inOrderList = testObjectFactory.makeWeatherData5DayListInOrder()
        val outOfOrderList = testObjectFactory.makeWeatherDataListOrganizedByTimestamp()
        assertEquals(inOrderList, organizeWeatherDataByDay(outOfOrderList))
    }

    @Test
    fun testOrganizeWeatherDataByDayEmptyList() {
        val emptyList = listOf<WeatherData>()
        assertEquals(null, organizeWeatherDataByDay(emptyList))
    }
}