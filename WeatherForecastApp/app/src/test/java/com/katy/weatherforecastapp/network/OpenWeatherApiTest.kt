package com.katy.weatherforecastapp.network

import com.katy.weatherforecastapp.model.remote.NetworkFiveDayForecast
import com.katy.weatherforecastapp.model.remote.NetworkLocation
import com.katy.weatherforecastapp.util.testUtil.testObjects.TestObjectFactory
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response


internal class OpenWeatherApiTest {
    private lateinit var openWeatherApi: OpenWeatherApi
    private val mockApiService = mockk<OpenWeatherApiService>()
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testObjectFactory = TestObjectFactory()
    val errorContent = "{\"error\": \"error\"}"


    @Before
    fun setup() {
        openWeatherApi = OpenWeatherApi(mockApiService, testDispatcher)
    }

    @After
    fun teardown() {
        unmockkAll()
    }

    @Test
    fun testGetLocationHappyPath() = runTest {
        coEvery { mockApiService.getLatLon("80303", any()) } returns
                Response.success(testObjectFactory.makeNetworkLocationObject())
        val expectedResponse = NetworkResult.Success(testObjectFactory.makeLocationObject())
        val response = openWeatherApi.getLocation("80303")
        assertEquals(expectedResponse, response)
    }

    @Test
    fun testGetLocationException() = runTest {
        coEvery { mockApiService.getLatLon("80303", any()) } throws Exception()
        val expectedResponse = NetworkResult.NetworkError
        val response = openWeatherApi.getLocation("80303")
        assertEquals(expectedResponse, response)
    }

    @Test
    fun testGetLocation404Error() = runTest {
        val mockResponse = Response.error<NetworkLocation>(
            404,
            ResponseBody.create("application/json".toMediaType(), errorContent)
        )
        coEvery { mockApiService.getLatLon("80303", any()) } returns mockResponse
        val expectedResponse = NetworkResult.BadRequest
        val response = openWeatherApi.getLocation("80303")
        assertEquals(expectedResponse, response)
    }

    @Test
    fun testGetLocationGeneralError() = runTest {
        val mockResponse = Response.error<NetworkLocation>(
            400,
            ResponseBody.create("application/json".toMediaType(), errorContent)
        )
        coEvery { mockApiService.getLatLon("80303", any()) } returns mockResponse
        val expectedResponse = NetworkResult.NetworkError
        val response = openWeatherApi.getLocation("80303")
        assertEquals(expectedResponse, response)
    }

    @Test
    fun testGetFiveDayForecastHappyPath() = runTest {
        coEvery { mockApiService.getFiveDayForecast(any(), any(), any(), any()) } returns
                Response.success(testObjectFactory.makeNetwork5DayForecastUTC())
        val expectedResponse =
            NetworkResult.Success(testObjectFactory.makeWeatherData5DayListInOrder())
        val response = openWeatherApi.getFiveDayForecast("123", "123")
        assertEquals(expectedResponse, response)
    }

    @Test
    fun testGetFiveDayForecastException() = runTest {
        coEvery { mockApiService.getFiveDayForecast(any(), any(), any(), any()) } throws Exception()
        val expectedResponse = NetworkResult.NetworkError
        val response = openWeatherApi.getFiveDayForecast("123", "123")
        assertEquals(expectedResponse, response)
    }

    @Test
    fun testGetFiveDayForecastGeneralError() = runTest {
        val mockResponse = Response.error<NetworkFiveDayForecast>(
            400,
            ResponseBody.create("application/json".toMediaType(), errorContent)
        )
        coEvery {
            mockApiService.getFiveDayForecast(
                any(),
                any(),
                any(),
                any()
            )
        } returns mockResponse
        val expectedResponse = NetworkResult.NetworkError
        val response = openWeatherApi.getFiveDayForecast("123", "123")
        assertEquals(expectedResponse, response)
    }
}