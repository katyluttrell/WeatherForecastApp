package com.katy.weatherforecastapp.database.cleaning

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime
import java.time.Month

internal class DatabaseCleaningRoutinesTest {

    @Test
    fun testGetTodayAndNextFiveDays() {
        val today = LocalDateTime.of(2023, Month.JULY, 13, 0,0,0)
        val expectedSet = setOf("2023-07-13","2023-07-14", "2023-07-15", "2023-07-16", "2023-07-17", "2023-07-18" )
        val receivedSet = DatabaseCleaningRoutines.getTodayAndNextFiveDays(today)
        assertEquals(expectedSet, receivedSet)
    }
}