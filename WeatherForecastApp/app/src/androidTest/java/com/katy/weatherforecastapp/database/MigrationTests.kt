package com.katy.weatherforecastapp.database

import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.katy.weatherforecastapp.database.migrations.migration_1_2
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MigrationTests {

    private val dbName = "weather"

    @get:Rule
    val testHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        WeatherDatabase::class.java
    )

    @Test
    fun testMigration1to2Data() {
        val db = testHelper.createDatabase(dbName, 1)
        db.apply {
            execSQL("INSERT INTO location VALUES('Boulder', '39.9914', '-105.239')")
            close()
        }
        val newDb = testHelper.runMigrationsAndValidate(dbName, 2, true, migration_1_2)
        assert(newDb.version == 2)
        val cursor = newDb.query("SELECT * FROM location")
        val columnNames = cursor.columnNames
        val expectedColumnNames = listOf("zipcode", "locationName", "lat", "lon")
        assert(columnNames.toList().containsAll(expectedColumnNames))
        cursor.moveToFirst()
        assertEquals("nozip", cursor.getString(0))
        assertEquals("Boulder", cursor.getString(1))
        assertEquals("39.9914", cursor.getString(2))
        assertEquals("-105.239", cursor.getString(3))
        newDb.close()
    }

    @Test
    fun testMigration1to2NoData() {
        val db = testHelper.createDatabase(dbName, 1)
        val newDb = testHelper.runMigrationsAndValidate(dbName, 2, true, migration_1_2)
        assert(newDb.version == 2)
        val cursor = newDb.query("SELECT * FROM location")
        val columnNames = cursor.columnNames
        val expectedColumnNames = listOf("zipcode", "locationName", "lat", "lon")
        assert(columnNames.toList().containsAll(expectedColumnNames))
        cursor.moveToFirst()
        assert(cursor.count == 0)
        db.close()
        newDb.close()
    }
}