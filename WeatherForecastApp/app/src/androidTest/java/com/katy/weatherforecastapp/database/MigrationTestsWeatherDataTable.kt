package com.katy.weatherforecastapp.database

import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.katy.weatherforecastapp.adapter.DateAdapter
import com.katy.weatherforecastapp.database.migrations.migration_1_2
import com.katy.weatherforecastapp.util.testUtil.testObjects.TestObjectFactory
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MigrationTestsWeatherDataTable {

    val testObjectFactory = TestObjectFactory()

    private val dbName = "weather"

    @get:Rule
    val testHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        WeatherDatabase::class.java
    )

    @Test
    fun testMigration1to2Data() {
        val db = testHelper.createDatabase(dbName, 1)
        val date = DateAdapter().localDateTimeToString(testObjectFactory.makeDateTime())
        db.apply {
            execSQL("INSERT INTO weatherData VALUES('$date', 72.5, 60.1, 100.4, '0.2', 'rain', 'super heavy rain', '11d', 5.3, 15.0)")
            close()
        }
        val newDb = testHelper.runMigrationsAndValidate(dbName, 2, true, migration_1_2)
        assert(newDb.version == 2)
        val cursor = newDb.query("SELECT * FROM weatherData")
        val columnNames = cursor.columnNames
        val expectedColumnNames = listOf(
            "zipcode",
            "dtTxt",
            "temp",
            "tempMin",
            "tempMax",
            "humidity",
            "main",
            "description",
            "icon",
            "speed",
            "gust"
        )
        assert(columnNames.toList().containsAll(expectedColumnNames))
        cursor.moveToFirst()
        assertEquals("nozip", cursor.getString(0))
        assertEquals(date, cursor.getString(1))
        assert(72.5 == cursor.getDouble(2))
        assert(60.1 == cursor.getDouble(3))
        assert(100.4 == cursor.getDouble(4))
        assertEquals("0.2", cursor.getString(5))
        assertEquals("rain", cursor.getString(6))
        assertEquals("super heavy rain", cursor.getString(7))
        assertEquals("11d", cursor.getString(8))
        assert(5.3 == cursor.getDouble(9))
        assert(15.0 == cursor.getDouble(10))
        newDb.close()
    }

    @Test
    fun testMigration1to2NoData() {
        val db = testHelper.createDatabase(dbName, 1)
        val newDb = testHelper.runMigrationsAndValidate(dbName, 2, true, migration_1_2)
        assert(newDb.version == 2)
        val cursor = newDb.query("SELECT * FROM weatherData")
        val columnNames = cursor.columnNames
        val expectedColumnNames = listOf(
            "zipcode",
            "dtTxt",
            "temp",
            "tempMin",
            "tempMax",
            "humidity",
            "main",
            "description",
            "icon",
            "speed",
            "gust"
        )
        assert(columnNames.toList().containsAll(expectedColumnNames))
        cursor.moveToFirst()
        assert(cursor.count == 0)
        db.close()
        newDb.close()
    }
}