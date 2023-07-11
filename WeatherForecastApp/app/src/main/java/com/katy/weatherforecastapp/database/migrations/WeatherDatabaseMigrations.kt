package com.katy.weatherforecastapp.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


val migration_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE IF NOT EXISTS location_temp (zipcode TEXT PRIMARY KEY NOT NULL, locationName TEXT NOT NULL, lat TEXT NOT NULL, lon TEXT NOT NULL)")
        database.execSQL("INSERT OR REPLACE INTO location_temp (zipcode, locationName, lat, lon) SELECT 'nozip', locationName, lat, lon FROM location")
        database.execSQL("DROP TABLE location")
        database.execSQL("ALTER TABLE location_temp RENAME TO location")

        database.execSQL("CREATE TABLE IF NOT EXISTS weather_temp (zipcode TEXT NOT NULL, dtTxt TEXT NOT NULL, temp REAL NOT NULL, tempMin REAL NOT NULL, tempMax REAL NOT NULL, humidity TEXT NOT NULL, main TEXT NOT NULL, description TEXT NOT NULL, icon TEXT NOT NULL, speed REAL NOT NULL, gust REAL NOT NULL, PRIMARY KEY(zipcode, dtTxt))")
        database.execSQL("INSERT OR REPLACE INTO weather_temp (zipcode, dtTxt, temp, tempMin, tempMax, humidity, main, description, icon, speed, gust) SELECT 'nozip', dtTxt, temp, tempMin, tempMax, humidity, main, description, icon, speed, gust FROM weatherData")
        database.execSQL("DROP TABLE weatherData")
        database.execSQL("ALTER TABLE weather_temp RENAME TO weatherData")
    }
}
