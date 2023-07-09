package com.katy.weatherforecastapp.database

import android.app.Instrumentation
import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MigrationTests {

    @get:Rule
    val testHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        WeatherDatabase::class.java
    )

    @Test
    fun testMigration1to2(){
        val db = testHelper.createDatabase("weather", 1)


    }
}
