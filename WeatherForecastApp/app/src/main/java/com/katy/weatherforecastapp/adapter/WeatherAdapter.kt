package com.katy.weatherforecastapp.adapter

import com.katy.weatherforecastapp.model.Weather
import com.squareup.moshi.*


class WeatherAdapter : JsonAdapter<Weather>() {
    @FromJson
    override fun fromJson(reader: JsonReader): Weather? {
        reader.beginArray()
        val weather = Moshi.Builder().build().adapter(Weather::class.java).fromJson(reader)
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