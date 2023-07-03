package com.katy.weatherforecastapp.adapters

import com.katy.weatherforecastapp.model.Weather
import com.katy.weatherforecastapp.model.WeatherData
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import java.time.LocalDateTime

class WeatherDataListAdapter: JsonAdapter<List<List<WeatherData>>>() {

    @FromJson
    override fun fromJson(reader: JsonReader): List<List<WeatherData>> {
        val listOfDays = mutableListOf<List<WeatherData>>()
        var oneDayForecastList = mutableListOf<WeatherData>()
        var currentDay: Int? = null
        reader.beginArray()
        while (reader.hasNext()) {
            val weatherData = Moshi.Builder().add(DateAdapter()).add(WeatherAdapter()).build().adapter(WeatherData::class.java).fromJson(reader)
            val dataDay = weatherData?.dtTxt?.dayOfYear
            if(dataDay != currentDay){
                if(oneDayForecastList.isNotEmpty()){
                    listOfDays.add(oneDayForecastList)
                    oneDayForecastList = mutableListOf<WeatherData>()
                }
                currentDay = dataDay
            }
            weatherData?.let { oneDayForecastList.add(it) }
        }
        reader.endArray()
        return listOfDays
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: List<List<WeatherData>>?) {
        writer.beginArray()
        if (value != null) {
            val adapter = Moshi.Builder().build().adapter(WeatherData::class.java)
            for (day in value) {
                writer.beginArray()
                for (weatherData in day) {
                    adapter.toJson(writer, weatherData)
                }
                writer.endArray()
            }
        }
        writer.endArray()
    }
}