package com.katy.weatherforecastapp.util


fun String.capitalize(): String = this.lowercase().replaceFirstChar { it.titlecase() }