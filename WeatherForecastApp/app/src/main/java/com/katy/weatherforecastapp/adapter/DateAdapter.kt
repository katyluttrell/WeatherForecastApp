package com.katy.weatherforecastapp.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateAdapter(): JsonAdapter<LocalDateTime>() {
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    @FromJson
    override fun fromJson(reader: JsonReader): LocalDateTime? {
        return LocalDateTime.parse( reader.nextString(), formatter)
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: LocalDateTime?) {
        writer.value(value?.format(formatter))
    }
}