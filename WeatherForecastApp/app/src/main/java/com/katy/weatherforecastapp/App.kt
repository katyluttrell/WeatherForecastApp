package com.katy.weatherforecastapp

import android.app.Application
import com.katy.weatherforecastapp.database.cleaning.DatabaseCleaningRoutines
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltAndroidApp
class App : Application() {
}