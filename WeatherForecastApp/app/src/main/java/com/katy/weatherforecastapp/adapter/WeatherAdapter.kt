package com.katy.weatherforecastapp.adapter

import com.katy.weatherforecastapp.model.Weather
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi


class WeatherAdapter: JsonAdapter<Weather>() {
    @FromJson
    override fun fromJson(reader: JsonReader): Weather? {
        reader.beginArray()
        val weather =  Moshi.Builder().build().adapter(Weather::class.java).fromJson(reader)
        reader.endArray()
        return weather
    }
    @ToJson
    override fun toJson(writer: JsonWriter, value: Weather?) {
        writer.beginArray()
        Moshi.Builder().build().adapter(Weather::class.java).toJson(writer, value)
        writer.endArray()
    }
}