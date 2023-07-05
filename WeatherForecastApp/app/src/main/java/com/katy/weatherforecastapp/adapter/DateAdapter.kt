package com.katy.weatherforecastapp.adapter

import androidx.room.TypeConverter
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class DateAdapter: JsonAdapter<LocalDateTime>() {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    @FromJson
    override fun fromJson(reader: JsonReader): LocalDateTime? = stringToLocalDateTime( reader.nextString())


    @ToJson
    override fun toJson(writer: JsonWriter, value: LocalDateTime?) {
        writer.value(value?.let{localDateTimeToString(it)})
    }

    @TypeConverter
    fun stringToLocalDateTime(value: String): LocalDateTime? = LocalDateTime.parse(value, formatter)

    @TypeConverter
    fun localDateTimeToString(value: LocalDateTime): String? = value.format(formatter)
}