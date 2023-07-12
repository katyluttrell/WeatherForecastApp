package com.katy.weatherforecastapp.util

import com.katy.weatherforecastapp.R
import com.katy.weatherforecastapp.model.WeatherData
import com.katy.weatherforecastapp.testObjects.TestObjectFactory
import com.katy.weatherforecastapp.util.RecyclerViewAdapterUtils
import org.junit.Assert.assertEquals
import org.junit.Test


internal class RecyclerViewAdapterUtilsTest {

    private val testObjectFactory = TestObjectFactory()
    private val utils = RecyclerViewAdapterUtils()

    @Test
    fun testGetMiddleOrFirstTimeListOf8() {
        val data = testObjectFactory.makeOneDayOfWeatherData()
        val time = utils.getMiddleOrFirstTime(data)
        assertEquals(data[4], time)
    }

    @Test
    fun testGetMiddleOrFirstTimeListOfNot8() {
        val data = testObjectFactory.makeOneDayOfWeatherData().subList(3, 7)
        val time = utils.getMiddleOrFirstTime(data)
        assertEquals(data[0], time)
    }

    @Test
    fun testGetMiddleOrFirstTimeEmptyList() {
        val data = listOf<WeatherData>()
        val time = utils.getMiddleOrFirstTime(data)
        assertEquals(null, time)
    }

    @Test
    fun testSetWeatherImage() {
        //TODO: coil test
    }

    @Test
    fun testSetTempIconColorNormal() {
        val temp = 60.0
        val color = utils.getTempIconColor(temp)
        assertEquals(null, color)
    }

    @Test
    fun testSetTempIconColorHot() {
        val temp = 90.0
        val color = utils.getTempIconColor(temp)
        assertEquals(R.color.md_theme_light_error, color)
    }

    @Test
    fun testSetTempIconColorCold() {
        val temp = 20.0
        val color = utils.getTempIconColor(temp)
        assertEquals(R.color.md_theme_light_primary, color)
    }

    @Test
    fun testFormatDateToday() {
        val now = testObjectFactory.makeDateTime(1)

        val result = utils.formatDate(testObjectFactory.makeDateTime(2), now)

        assertEquals(R.string.today, result)
    }

    @Test
    fun testFormatDateTomorrow() {
        val now = testObjectFactory.makeDateTime(1)

        val result = utils.formatDate(testObjectFactory.makeDateTime(3), now)

        assertEquals(R.string.tomorrow, result)
    }

    @Test
    fun testFormatDateOtherDay() {
        val now = testObjectFactory.makeDateTime(1)

        val result = utils.formatDate(testObjectFactory.makeDateTime(15), now)

        assertEquals(null, result)
    }

    @Test
    fun testFormatTimeAm(){
        val time = testObjectFactory.makeDateTime(3)
        assertEquals("12:00 AM", utils.formatTime(time))
    }

    @Test
    fun testFormatTimePm(){
        val time = testObjectFactory.makeDateTime(1)
        assertEquals("6:00 PM", utils.formatTime(time))
    }


}