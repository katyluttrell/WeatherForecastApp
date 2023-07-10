package com.katy.weatherforecastapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.katy.weatherforecastapp.adapter.DateAdapter
import com.katy.weatherforecastapp.database.dao.LocationDao
import com.katy.weatherforecastapp.database.dao.WeatherDataDao
import com.katy.weatherforecastapp.database.migrations.migration_1_2
import com.katy.weatherforecastapp.model.local.LocationEntity
import com.katy.weatherforecastapp.model.local.WeatherDataEntity

const val DATABASE_VERSION = 2

@Database(
    entities = [LocationEntity::class, WeatherDataEntity::class],
    version = DATABASE_VERSION,
    exportSchema = true
)
@TypeConverters(DateAdapter::class)
abstract class WeatherDatabase : RoomDatabase() {
    companion object {
        private const val DATABASE_NAME = "Weather"
        fun buildDatabase(context: Context): WeatherDatabase {
            return Room.databaseBuilder(
                context,
                WeatherDatabase::class.java,
                DATABASE_NAME
            )
                .addMigrations(migration_1_2)
                .build()
        }
    }

    abstract fun locationDao(): LocationDao
    abstract fun weatherDataDao(): WeatherDataDao
}