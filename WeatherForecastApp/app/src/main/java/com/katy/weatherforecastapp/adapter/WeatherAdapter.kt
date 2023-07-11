package com.katy.weatherforecastapp.adapter

import com.katy.weatherforecastapp.model.remote.NetworkWeather
import com.squareup.moshi.*


class WeatherAdapter : JsonAdapter<NetworkWeather>() {
    @FromJson
    override fun fromJson(reader: JsonReader): NetworkWeather? {
        reader.beginArray()
        val weather = Moshi.Builder().build().adapter(NetworkWeather::class.java).fromJson(reader)
        reader.endArray()
        return weather
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: NetworkWeather?) {
        writer.beginArray()
        Moshi.Builder().build().adapter(NetworkWeather::class.java).toJson(writer, value)
        writer.endArray()
    }
}