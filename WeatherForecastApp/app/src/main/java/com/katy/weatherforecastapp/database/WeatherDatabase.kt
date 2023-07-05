package com.katy.weatherforecastapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.katy.weatherforecastapp.adapter.DateAdapter
import com.katy.weatherforecastapp.database.dao.LocationDao
import com.katy.weatherforecastapp.database.dao.WeatherDataDao
import com.katy.weatherforecastapp.model.Location
import com.katy.weatherforecastapp.model.WeatherData

const val DATABASE_VERSION = 1
@Database(
    entities = [Location::class, WeatherData::class],
    version = DATABASE_VERSION
)
@TypeConverters(DateAdapter::class)
abstract class WeatherDatabase: RoomDatabase() {
    companion object{
        private const val DATABASE_NAME = "Weather"
        fun buildDatabase(context: Context): WeatherDatabase{
            return Room.databaseBuilder(
                context,
                WeatherDatabase::class.java,
                DATABASE_NAME)
                .build()
        }
    }

    abstract fun locationDao(): LocationDao
    abstract fun weatherDataDao(): WeatherDataDao
}