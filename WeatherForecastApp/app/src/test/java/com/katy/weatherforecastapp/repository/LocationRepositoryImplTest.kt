package com.katy.weatherforecastapp.repository

import android.content.Context
import com.katy.weatherforecastapp.database.dao.LocationDao
import com.katy.weatherforecastapp.model.Location
import com.katy.weatherforecastapp.network.NetworkResult
import com.katy.weatherforecastapp.network.NetworkUtils
import com.katy.weatherforecastapp.network.OpenWeatherApi
import com.katy.weatherforecastapp.util.testUtil.testObjects.TestObjectFactory
import io.mockk.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
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

    private val callbacks = object : LocationDataErrorCallbacks {
        override fun onInvalidZipcode() {
            onInvalidZipcodeTest = true
        }

        override fun onNetworkError() {
            onNetworkErrorTest = true
        }

        override fun onNoInternetNoData() {
            onNoInternetNoDataTest = true
        }
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
        testDispatcher = StandardTestDispatcher()

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
    fun teardown() {
        unmockkAll()
    }

    @Test
    fun testCacheLocation() = runTest(testDispatcher) {
        val input = testObjectFactory.makeLocationObject()
        val outputData = testObjectFactory.makeLocationEntityObject()

        locationRepositoryImpl.cacheLocation(input)

        verify { mockLocationDao.addLocation(outputData) }
    }

    @Test
    fun testGetLocationFlowHappyPath() = runTest(testDispatcher) {
        val expectedResult = testObjectFactory.makeLocationObject()
        every { mockLocationDao.getLocation("80303") } returns
                flow {
                    emit(testObjectFactory.makeLocationEntityObject())
                }
        var returnedResult: Location?


        returnedResult = locationRepositoryImpl.getLocationFlow("80303", callbacks).first()

        assertEquals(expectedResult, returnedResult)
        assertFalse(onInvalidZipcodeTest)
        assertFalse(onNetworkErrorTest)
        assertFalse(onNoInternetNoDataTest)
    }

    @Test
    fun testGetLocationFlowNullResultAndInternet() {
        every { mockNetworkUtils.hasInternetAccess(any()) } returns true
        coEvery { locationRepositoryImpl.fetchLocation(any(), any()) } returns Unit
        val expectedResult = 0
        every { mockLocationDao.getLocation("80303") } returns
                flow { emit(null) }
        var returnedResult: Int

        runBlocking {
            returnedResult = locationRepositoryImpl.getLocationFlow("80303", callbacks).count()
        }

        assertEquals(expectedResult, returnedResult)
        coVerify { locationRepositoryImpl.fetchLocation("80303", callbacks) }
        assertFalse(onInvalidZipcodeTest)
        assertFalse(onNetworkErrorTest)
        assertFalse(onNoInternetNoDataTest)
    }

    @Test
    fun testGetLocationFlowNullResultNoInternet() = runTest(testDispatcher) {
        every { mockNetworkUtils.hasInternetAccess(any()) } returns false
        coEvery { locationRepositoryImpl.fetchLocation(any(), any()) } returns Unit
        val expectedResult = 0
        every { mockLocationDao.getLocation("80303") } returns
                flow { emit(null) }
        var returnedResult: Int


        returnedResult = locationRepositoryImpl.getLocationFlow("80303", callbacks).count()


        assertEquals(expectedResult, returnedResult)
        coVerify(inverse = true) { locationRepositoryImpl.fetchLocation("80303", callbacks) }
        assertFalse(onInvalidZipcodeTest)
        assertFalse(onNetworkErrorTest)
        assertTrue(onNoInternetNoDataTest)
    }

    @Test
    fun testFetchLocationHappyPath() = runTest(testDispatcher) {
        val data = testObjectFactory.makeLocationObject()
        coEvery { mockOpenWeatherApi.getLocation("80303") } returns
                NetworkResult.Success(data)
        coEvery { locationRepositoryImpl.cacheLocation(any()) } returns Unit

        locationRepositoryImpl.fetchLocation("80303", callbacks)

        coVerify { locationRepositoryImpl.cacheLocation(data) }
        coVerify { locationRepositoryImpl.cacheLocation(data) }
        assertFalse(onInvalidZipcodeTest)
        assertFalse(onNetworkErrorTest)
        assertFalse(onNoInternetNoDataTest)
    }

    @Test
    fun testFetchLocationBadRequest() = runTest(testDispatcher) {
        coEvery { mockOpenWeatherApi.getLocation("80303") } returns
                NetworkResult.BadRequest
        coEvery { locationRepositoryImpl.cacheLocation(any()) } returns Unit

        locationRepositoryImpl.fetchLocation("80303", callbacks)

        coVerify(inverse = true) { locationRepositoryImpl.cacheLocation(any()) }
        assertTrue(onInvalidZipcodeTest)
        assertFalse(onNetworkErrorTest)
        assertFalse(onNoInternetNoDataTest)
    }

    @Test
    fun testFetchLocationNetworkError() = runTest(testDispatcher) {
        val data = testObjectFactory.makeLocationObject()
        coEvery { mockOpenWeatherApi.getLocation("80303") } returns
                NetworkResult.NetworkError
        coEvery { locationRepositoryImpl.cacheLocation(any()) } returns Unit

        locationRepositoryImpl.fetchLocation("80303", callbacks)

        coVerify(inverse = true) { locationRepositoryImpl.cacheLocation(data) }
        assertFalse(onInvalidZipcodeTest)
        assertTrue(onNetworkErrorTest)
        assertFalse(onNoInternetNoDataTest)
    }
}