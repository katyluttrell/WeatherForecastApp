package com.katy.weatherforecastapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.katy.weatherforecastapp.R
import com.katy.weatherforecastapp.model.WeatherData
import com.katy.weatherforecastapp.network.LinkFactory
import com.katy.weatherforecastapp.utils.Utils

class DayForecastAdapter(private val dataList: List<List<WeatherData>>, private val context:Context): RecyclerView.Adapter<DayForecastAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.day_forecast_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = dataList[position]
        val data = getMiddleOrFirstTime(day)
        setWeatherImage(holder.weatherImage, data.weather.icon)
        holder.dateText.text = Utils.formatDate(data.dtTxt, context)
        holder.weatherText.text = data.weather.main
        holder.tempText.text =  context.getString(R.string.temp_format,data.main.temp.toInt())
        setTempIconColor(holder.tempIcon, data.main.temp)
        holder.windText.text = context.getString(R.string.wind_text_format_mph, data.wind.speed.toInt())
        holder.itemView.setOnClickListener { onClick(holder, dataList[position]) }
    }

    private fun getMiddleOrFirstTime(day: List<WeatherData>): WeatherData {
        return if(day.size == 8){
            day[4]
        }else {
            day[0]
        }
    }


    private fun setWeatherImage(weatherImage: ImageView, icon: String) {
        val url = LinkFactory().openWeatherIconLink(icon)
        weatherImage.load(url) {
            size(350, 350)
            placeholder(R.drawable.baseline_sync)
            error(R.drawable.baseline_sync_problem_24)
        }
    }

    private fun setTempIconColor(tempIcon: ImageView, temp: Double) {
        when {
            temp <= 32.0 -> {
                tempIcon.contentDescription = context.getString(R.string.blue_thermostat_icon)
                tempIcon.setColorFilter(ContextCompat.getColor(context, R.color.md_theme_light_primary))
            }
            temp >= 85.0 -> {
                tempIcon.contentDescription = context.getString(R.string.red_thermostat_icon)
                val color = ContextCompat.getColor(context,R.color.md_theme_light_error)
                tempIcon.setColorFilter(color)
            }
            else -> {
                tempIcon.contentDescription = context.getString(R.string.thermostat_icon_content_description)
                tempIcon.clearColorFilter()
            }
        }
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val weatherImage: ImageView = ItemView.findViewById(R.id.weatherImage)
        val dateText: TextView = ItemView.findViewById(R.id.dateText)
        val weatherText: TextView = ItemView.findViewById(R.id.weatherText)
        val tempIcon: ImageView = ItemView.findViewById(R.id.tempIcon)
        val tempText: TextView = ItemView.findViewById(R.id.tempText)
        val windText: TextView = ItemView.findViewById(R.id.windText)
    }

    fun onClick(holder: ViewHolder, dayData: List<WeatherData>){
        //TODO: new fragment with additional weather info
    }


}

