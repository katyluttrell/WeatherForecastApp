package com.katy.weatherforecastapp.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


val migration_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE location_temp (zipcode TEXT PRIMARY KEY NOT NULL, locationName TEXT NOT NULL, lat TEXT NOT NULL, lon TEXT NOT NULL)")
        database.execSQL("INSERT OR REPLACE INTO location_temp (zipcode, locationName, lat, lon) SELECT 'nozip', locationName, lat, lon FROM location")
        database.execSQL("DROP TABLE location")
        database.execSQL("ALTER TABLE location_temp RENAME TO location")
    }
}
