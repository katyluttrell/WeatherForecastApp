package com.katy.weatherforecastapp.database.cleaning

import androidx.annotation.VisibleForTesting
import com.katy.weatherforecastapp.adapter.DateAdapter
import com.katy.weatherforecastapp.database.dao.WeatherDataDao
import com.katy.weatherforecastapp.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject

class DatabaseCleaningRoutines @Inject constructor(
    private val weatherDataDao: WeatherDataDao,
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
) {
    val hasCleaned: Boolean
        get() = hasCleanedState

    private var hasCleanedState = false

    suspend fun cleanDatabase(){
        hasCleanedState = true
        val keepDays = getTodayAndNextFiveDays()
        withContext(ioDispatcher){
            weatherDataDao.deleteOldData(keepDays)
        }
    }

    companion object {
        internal fun getTodayAndNextFiveDays(today: LocalDateTime = LocalDateTime.now()): Set<String> {
            val dateAdapter = DateAdapter()
            val dateList = mutableListOf<String>()
            for (i in 0..5) {
                val dayString =
                    dateAdapter.localDateTimeToString(today.plusDays(i.toLong()))?.substring(0, 10)
                dayString?.let { dateList.add(it) }
            }
            return dateList.toSet()
        }
    }
}

