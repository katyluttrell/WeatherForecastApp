package com.katy.weatherforecastapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.katy.weatherforecastapp.database.cleaning.DatabaseCleaningRoutines
import com.katy.weatherforecastapp.repository.LocationRepository
import com.katy.weatherforecastapp.repository.WeatherRepository
import com.katy.weatherforecastapp.util.testUtil.testObjects.TestObjectFactory
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class MainViewModelTest {
    private val testObjectFactory = TestObjectFactory()
    private lateinit var mainViewModel: MainViewModel
    private val mockLocationRepository: LocationRepository = mockk<LocationRepository>()
    private val mockWeatherRepository: WeatherRepository = mockk<WeatherRepository>()
    private val mockDatabaseCleaningRoutines: DatabaseCleaningRoutines =
        mockk<DatabaseCleaningRoutines>()

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mainViewModel = spyk(
            MainViewModel(
                mockLocationRepository,
                mockWeatherRepository,
                mockDatabaseCleaningRoutines,
                testDispatcher
            )
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }


    @Test
    fun testStartObservingLocationDataHappyPath() {
        val location = testObjectFactory.makeLocationObject()
        val locationFlow = flow { emit(location) }
        every { mainViewModel.startObservingWeatherData(any()) } returns Unit
        coEvery { mockLocationRepository.getLocationFlow(any(), any()) } returns locationFlow

        mainViewModel.startObservingLocationData("80303")

        testDispatcher.scheduler.advanceUntilIdle()
        assert(mainViewModel.location.isInitialized)
        assertEquals(location, mainViewModel.location.value)
        verify { mainViewModel.startObservingWeatherData(location) }
    }

    @Test
    fun testStartObservingLocationDataHappyPathDataUpdate() {
        val location1 = testObjectFactory.makeLocationObject()
        val location2 = testObjectFactory.makeLocationObject()
        val locationFlow = flow {
            emit(location1)
            emit(location2)
        }
        every { mainViewModel.startObservingWeatherData(any()) } returns Unit
        coEvery { mockLocationRepository.getLocationFlow(any(), any()) } returns locationFlow

        mainViewModel.startObservingLocationData("80303")

        testDispatcher.scheduler.advanceUntilIdle()
        assert(mainViewModel.location.isInitialized)
        assertEquals(location2, mainViewModel.location.value)
        verify { mainViewModel.startObservingWeatherData(location2) }
    }

    @Test
    fun testStartObservingLocationDataEmptyFlow() {
        val locationFlow = flow { emit(null) }.filterNotNull()
        every { mainViewModel.startObservingWeatherData(any()) } returns Unit
        coEvery { mockLocationRepository.getLocationFlow(any(), any()) } returns locationFlow

        mainViewModel.startObservingLocationData("80303")

        testDispatcher.scheduler.advanceUntilIdle()
        assert(!mainViewModel.location.isInitialized)
        verify(inverse = true) { mainViewModel.startObservingWeatherData(any()) }
    }

    @Test
    fun testStartObservingWeatherDataHappyPath() {
        val location = testObjectFactory.makeLocationObject()
        val weather = testObjectFactory.makeWeatherData5DayListInOrder()
        val weatherFlow = flow { emit(weather) }
        coEvery {
            mockWeatherRepository.getFiveDayForecastListFlow(
                any(),
                any()
            )
        } returns weatherFlow

        mainViewModel.startObservingWeatherData(location)

        testDispatcher.scheduler.advanceUntilIdle()
        assert(mainViewModel.weatherDataList.isInitialized)
        assertEquals(weather, mainViewModel.weatherDataList.value)
    }

    @Test
    fun testStartObservingWeatherDataHappyPathDataUpdate() {
        val location = testObjectFactory.makeLocationObject()
        val weather = testObjectFactory.makeWeatherData5DayListInOrder()
        val weatherFlow = flow {
            emit(weather.subList(1, 3))
            emit(weather)
        }
        coEvery {
            mockWeatherRepository.getFiveDayForecastListFlow(
                any(),
                any()
            )
        } returns weatherFlow

        mainViewModel.startObservingWeatherData(location)

        testDispatcher.scheduler.advanceUntilIdle()
        assert(mainViewModel.weatherDataList.isInitialized)
        assertEquals(weather, mainViewModel.weatherDataList.value)
    }

    @Test
    fun testStartObservingWeatherDataEmptyFlow() {
        val location = testObjectFactory.makeLocationObject()
        val weather = testObjectFactory.makeWeatherData5DayListInOrder()
        val weatherFlow = flow { emit(null) }.filterNotNull()
        coEvery {
            mockWeatherRepository.getFiveDayForecastListFlow(
                any(),
                any()
            )
        } returns weatherFlow

        mainViewModel.startObservingWeatherData(location)

        testDispatcher.scheduler.advanceUntilIdle()
        assert(!mainViewModel.weatherDataList.isInitialized)
    }

    @Test
    fun cleanDatabaseNeedsCleaning() {
        every { mockDatabaseCleaningRoutines.hasCleaned } returns false
        coEvery { mockDatabaseCleaningRoutines.cleanDatabase() } returns Unit

        mainViewModel.cleanDatabase()

        testDispatcher.scheduler.advanceUntilIdle()
        coVerify { mockDatabaseCleaningRoutines.cleanDatabase() }
    }

    @Test
    fun cleanDatabaseAlreadyClean() {
        every { mockDatabaseCleaningRoutines.hasCleaned } returns true
        coEvery { mockDatabaseCleaningRoutines.cleanDatabase() } returns Unit

        mainViewModel.cleanDatabase()

        testDispatcher.scheduler.advanceUntilIdle()
        coVerify(inverse = true) { mockDatabaseCleaningRoutines.cleanDatabase() }
    }
}