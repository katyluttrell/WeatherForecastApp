package com.katy.weatherforecastapp.repository

import android.content.Context
import com.katy.weatherforecastapp.database.dao.LocationDao
import com.katy.weatherforecastapp.database.dao.WeatherDataDao
import com.katy.weatherforecastapp.model.Location
import com.katy.weatherforecastapp.model.WeatherData
import com.katy.weatherforecastapp.model.local.WeatherDataEntity
import com.katy.weatherforecastapp.network.NetworkResult
import com.katy.weatherforecastapp.network.NetworkUtils
import com.katy.weatherforecastapp.network.OpenWeatherApi
import com.katy.weatherforecastapp.testObjects.TestObjectFactory
import io.mockk.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.withTimeout
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test


internal class WeatherRepositoryImplTest{
    private val testObjectFactory = TestObjectFactory()

    private lateinit var weatherRepositoryImpl: WeatherRepositoryImpl

    private lateinit var mockWeatherDataDao: WeatherDataDao
    private lateinit var mockOpenWeatherApi: OpenWeatherApi
    private lateinit var mockNetworkUtils: NetworkUtils
    private lateinit var mockContext: Context
    private lateinit var testDispatcher: CoroutineDispatcher

    private var onNetworkErrorTest = false
    private var onNoInternetNoDataTest = false

    private val callbacks = object: WeatherDataErrorCallbacks{
        override fun onNetworkError() { onNetworkErrorTest = true }
        override fun onNoInternetNoData() { onNoInternetNoDataTest = true }
    }

    @Before
    fun setup() {
        onNetworkErrorTest = false
        onNoInternetNoDataTest = false

        mockWeatherDataDao = mockk<WeatherDataDao>(relaxed = true)
        mockOpenWeatherApi = mockk<OpenWeatherApi>()
        mockNetworkUtils = mockk<NetworkUtils>()
        mockContext = mockk<Context>()
        testDispatcher = UnconfinedTestDispatcher()

        weatherRepositoryImpl = spyk(
            WeatherRepositoryImpl(
                mockWeatherDataDao,
                mockOpenWeatherApi,
                mockNetworkUtils,
                mockContext,
                testDispatcher
            )
        )
    }

    @After
    fun teardown(){
        unmockkAll()
    }

    @Test
    fun testCacheFiveDayForecastList() {
        val input = testObjectFactory.makeWeatherData5DayListInOrder()
        val outputData = testObjectFactory.makeWeatherDataEntity5DayListInOrder()
        runBlocking { weatherRepositoryImpl.cacheFiveDayForecastList(input, "80303") }

        for(data in outputData.flatten()){
            verify(exactly = 1) { mockWeatherDataDao.addWeatherData(data) }
        }
    }

    @Test
    fun testGetWeatherFlowHappyPath() {
        val expectedResult = testObjectFactory.makeWeatherData5DayListInOrder()
        val location = testObjectFactory.makeLocationObject()
        every { mockWeatherDataDao.getWeatherData("80303") } returns
                flow {
                    emit(testObjectFactory.makeWeatherDataEntityListOutOfOrder())
                }
        var returnedResult: List<List<WeatherData>>?
        runBlocking { returnedResult = weatherRepositoryImpl.getFiveDayForecastListFlow(location, callbacks).first()}

        coVerify(inverse = true) { weatherRepositoryImpl.fetchFiveDayForecast(any(), any()) }
        Assert.assertEquals(expectedResult, returnedResult)
        Assert.assertFalse(onNetworkErrorTest)
        Assert.assertFalse(onNoInternetNoDataTest)
    }

    @Test
    fun testGetWeatherFlowEmptyListAndInternet() {
        val location = testObjectFactory.makeLocationObject()
        every{mockNetworkUtils.hasInternetAccess(any())} returns true
        coEvery{weatherRepositoryImpl.fetchFiveDayForecast(location, callbacks)} returns Unit
        val expectedResult = 0
        every { mockWeatherDataDao.getWeatherData("80303") } returns
                flow { emit(listOf<WeatherDataEntity>()) }
        var returnedResult: Int
        runBlocking { returnedResult = weatherRepositoryImpl.getFiveDayForecastListFlow(location, callbacks).count()}

        Assert.assertEquals(expectedResult, returnedResult)
        coVerify { weatherRepositoryImpl.fetchFiveDayForecast(location, callbacks) }
        Assert.assertFalse(onNetworkErrorTest)
        Assert.assertFalse(onNoInternetNoDataTest)
    }

    @Test
    fun testGetWeatherFlowEmptyListNoInternet() {
        val location = testObjectFactory.makeLocationObject()
        every{mockNetworkUtils.hasInternetAccess(any())} returns false
        coEvery{weatherRepositoryImpl.fetchFiveDayForecast(location, callbacks)} returns Unit
        val expectedResult = 0
        every { mockWeatherDataDao.getWeatherData("80303") } returns
                flow { emit(listOf<WeatherDataEntity>()) }
        var returnedResult: Int
        runBlocking { returnedResult = weatherRepositoryImpl.getFiveDayForecastListFlow(location, callbacks).count()}

        Assert.assertEquals(expectedResult, returnedResult)
        coVerify(inverse = true) { weatherRepositoryImpl.fetchFiveDayForecast(any(), any()) }
        Assert.assertFalse(onNetworkErrorTest)
        Assert.assertTrue(onNoInternetNoDataTest)
    }

    @Test
    fun testFetchFiveDayForecastHappyPath(){
        val loc = testObjectFactory.makeLocationObject()
        val data = testObjectFactory.makeWeatherData5DayListInOrder()
        coEvery { mockOpenWeatherApi.getFiveDayForecast(loc.lat, loc.lon) } returns
                NetworkResult.Success(data)
        coEvery { weatherRepositoryImpl.cacheFiveDayForecastList(any(), any()) } returns Unit

        runBlocking { weatherRepositoryImpl.fetchFiveDayForecast(loc, callbacks) }

        coVerify { weatherRepositoryImpl.cacheFiveDayForecastList(data, loc.zipcode) }
        Assert.assertFalse(onNetworkErrorTest)
        Assert.assertFalse(onNoInternetNoDataTest)
    }

    @Test
    fun testFetchFiveDayForecastNetworkError(){
        val loc = testObjectFactory.makeLocationObject()
        coEvery { mockOpenWeatherApi.getFiveDayForecast(loc.lat, loc.lon) } returns
                NetworkResult.NetworkError

        runBlocking { weatherRepositoryImpl.fetchFiveDayForecast(loc, callbacks) }

        coVerify(inverse=true) { weatherRepositoryImpl.cacheFiveDayForecastList(any(), any()) }
        Assert.assertTrue(onNetworkErrorTest)
        Assert.assertFalse(onNoInternetNoDataTest)
    }

    @Test
    fun testFetchFiveDayForecastEmptyList(){
        val loc = testObjectFactory.makeLocationObject()
        coEvery { mockOpenWeatherApi.getFiveDayForecast(loc.lat, loc.lon) } returns
                NetworkResult.Success(listOf<List<WeatherData>>())

        runBlocking { weatherRepositoryImpl.fetchFiveDayForecast(loc, callbacks) }

        coVerify(inverse=true) { weatherRepositoryImpl.cacheFiveDayForecastList(any(), any()) }
        Assert.assertTrue(onNetworkErrorTest)
        Assert.assertFalse(onNoInternetNoDataTest)
    }

    @Test
    fun testFetchFiveDayForecastEmptyListList(){
        val loc = testObjectFactory.makeLocationObject()
        coEvery { mockOpenWeatherApi.getFiveDayForecast(loc.lat, loc.lon) } returns
                NetworkResult.Success(listOf(listOf<WeatherData>()))

        runBlocking { weatherRepositoryImpl.fetchFiveDayForecast(loc, callbacks) }

        coVerify(inverse=true) { weatherRepositoryImpl.cacheFiveDayForecastList(any(), any()) }
        Assert.assertTrue(onNetworkErrorTest)
        Assert.assertFalse(onNoInternetNoDataTest)
    }

    @Test
    fun testFetchFiveDayForecastListOfOther(){
        val loc = testObjectFactory.makeLocationObject()
        coEvery { mockOpenWeatherApi.getFiveDayForecast(loc.lat, loc.lon) } returns
                NetworkResult.Success(listOf("hi"))

        runBlocking { weatherRepositoryImpl.fetchFiveDayForecast(loc, callbacks) }

        coVerify(inverse=true) { weatherRepositoryImpl.cacheFiveDayForecastList(any(), any()) }
        Assert.assertTrue(onNetworkErrorTest)
        Assert.assertFalse(onNoInternetNoDataTest)
    }

    @Test
    fun testFetchFiveDayForecastListListOfOther(){
        val loc = testObjectFactory.makeLocationObject()
        coEvery { mockOpenWeatherApi.getFiveDayForecast(loc.lat, loc.lon) } returns
                NetworkResult.Success(listOf(listOf("hi")))

        runBlocking { weatherRepositoryImpl.fetchFiveDayForecast(loc, callbacks) }

        coVerify(inverse=true) { weatherRepositoryImpl.cacheFiveDayForecastList(any(), any()) }
        Assert.assertTrue(onNetworkErrorTest)
        Assert.assertFalse(onNoInternetNoDataTest)
    }

    @Test
    fun testDeleteAllWeatherData(){
        every { mockWeatherDataDao.deleteAll() } returns Unit

        runBlocking { weatherRepositoryImpl.deleteAllWeatherData() }

        verify { mockWeatherDataDao.deleteAll() }
    }
}