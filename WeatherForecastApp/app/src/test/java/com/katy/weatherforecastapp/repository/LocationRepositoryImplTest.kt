package com.katy.weatherforecastapp.repository

import android.content.Context
import com.katy.weatherforecastapp.database.dao.LocationDao
import com.katy.weatherforecastapp.model.Location
import com.katy.weatherforecastapp.network.NetworkResult
import com.katy.weatherforecastapp.network.NetworkUtils
import com.katy.weatherforecastapp.network.OpenWeatherApi
import com.katy.weatherforecastapp.testObjects.TestObjectFactory
import io.mockk.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

internal class LocationRepositoryImplTest {
    private val testObjectFactory = TestObjectFactory()

    private lateinit var locationRepositoryImpl: LocationRepositoryImpl

    private lateinit var mockLocationDao: LocationDao
    private lateinit var mockOpenWeatherApi: OpenWeatherApi
    private lateinit var mockNetworkUtils: NetworkUtils
    private lateinit var mockContext: Context
    private lateinit var testDispatcher: CoroutineDispatcher

    private var onInvalidZipcodeTest = false
    private var onNetworkErrorTest = false
    private var onNoInternetNoDataTest = false

    private val callbacks = object :LocationDataErrorCallbacks{
        override fun onInvalidZipcode() { onInvalidZipcodeTest = true }
        override fun onNetworkError() { onNetworkErrorTest = true }
        override fun onNoInternetNoData() { onNoInternetNoDataTest = true }
    }

    @Before
    fun setup() {
        onInvalidZipcodeTest = false
        onNetworkErrorTest = false
        onNoInternetNoDataTest = false

        mockLocationDao = mockk<LocationDao>(relaxed = true)
        mockOpenWeatherApi = mockk<OpenWeatherApi>()
        mockNetworkUtils = mockk<NetworkUtils>()
        mockContext = mockk<Context>()
        testDispatcher = UnconfinedTestDispatcher()

        locationRepositoryImpl = spyk(
            LocationRepositoryImpl(
                mockLocationDao,
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
    fun testCacheLocation() {
        val input = testObjectFactory.makeLocationObject()
        val outputData = testObjectFactory.makeLocationEntityObject()
        runBlocking { locationRepositoryImpl.cacheLocation(input) }
        verify { mockLocationDao.addLocation(outputData) }
    }

    @Test
    fun testGetLocationFlowHappyPath() {
        val expectedResult = testObjectFactory.makeLocationObject()
        every { mockLocationDao.getLocation("80303") } returns
                flow {
                    emit(testObjectFactory.makeLocationEntityObject())
                }
        var returnedResult: Location?
        runBlocking { returnedResult = locationRepositoryImpl.getLocationFlow("80303", callbacks).first()}

        assertEquals(expectedResult, returnedResult)
        assertFalse(onInvalidZipcodeTest)
        assertFalse(onNetworkErrorTest)
        assertFalse(onNoInternetNoDataTest)
    }

    @Test
    fun testGetLocationFlowNullResultAndInternet() {
        every{mockNetworkUtils.hasInternetAccess(any())} returns true
        coEvery{locationRepositoryImpl.fetchLocation(any(), any())} returns Unit
        val expectedResult = 0
        every { mockLocationDao.getLocation("80303") } returns
                flow { emit(null) }
        var returnedResult: Int
        runBlocking { returnedResult = locationRepositoryImpl.getLocationFlow("80303", callbacks).count()}

        assertEquals(expectedResult, returnedResult)
        assertFalse(onInvalidZipcodeTest)
        assertFalse(onNetworkErrorTest)
        assertFalse(onNoInternetNoDataTest)
    }

    @Test
    fun testGetLocationFlowNullResultNoInternet() {
        every{mockNetworkUtils.hasInternetAccess(any())} returns false
        coEvery{locationRepositoryImpl.fetchLocation(any(), any())} returns Unit
        val expectedResult = 0
        every { mockLocationDao.getLocation("80303") } returns
                flow { emit(null) }
        var returnedResult: Int
        runBlocking { returnedResult = locationRepositoryImpl.getLocationFlow("80303", callbacks).count()}

        assertEquals(expectedResult, returnedResult)
        assertFalse(onInvalidZipcodeTest)
        assertFalse(onNetworkErrorTest)
        assertTrue(onNoInternetNoDataTest)
    }

    @Test
    fun testFetchLocationHappyPath(){
        val data = testObjectFactory.makeLocationObject()
        coEvery { mockOpenWeatherApi.getLocation("80303") } returns
                NetworkResult.Success(data)
        coEvery { locationRepositoryImpl.cacheLocation(any()) } returns Unit
        runBlocking { locationRepositoryImpl.fetchLocation("80303", callbacks) }
        coVerify { locationRepositoryImpl.cacheLocation(data) }

        assertFalse(onInvalidZipcodeTest)
        assertFalse(onNetworkErrorTest)
        assertFalse(onNoInternetNoDataTest)
    }

    @Test
    fun testFetchLocationBadRequest(){
        val data = testObjectFactory.makeLocationObject()
        coEvery { mockOpenWeatherApi.getLocation("80303") } returns
                NetworkResult.BadRequest
        coEvery { locationRepositoryImpl.cacheLocation(any()) } returns Unit
        runBlocking { locationRepositoryImpl.fetchLocation("80303", callbacks) }

        coVerify(inverse = true) { locationRepositoryImpl.cacheLocation(data) }
        assertTrue(onInvalidZipcodeTest)
        assertFalse(onNetworkErrorTest)
        assertFalse(onNoInternetNoDataTest)
    }

    @Test
    fun testFetchLocationNetworkError(){
        val data = testObjectFactory.makeLocationObject()
        coEvery { mockOpenWeatherApi.getLocation("80303") } returns
                NetworkResult.NetworkError
        coEvery { locationRepositoryImpl.cacheLocation(any()) } returns Unit
        runBlocking { locationRepositoryImpl.fetchLocation("80303", callbacks) }

        coVerify(inverse = true) { locationRepositoryImpl.cacheLocation(data) }
        assertFalse(onInvalidZipcodeTest)
        assertTrue(onNetworkErrorTest)
        assertFalse(onNoInternetNoDataTest)
    }
}