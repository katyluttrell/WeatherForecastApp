package com.katy.weatherforecastapp.adapter

import android.app.Activity
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
import com.katy.weatherforecastapp.util.Utils

class TimeForecastAdapter(private val dataList: List<WeatherData>, private val activity: Activity) :
    RecyclerView.Adapter<TimeForecastAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.day_forecast_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        setWeatherImage(holder.weatherImage, data.weather.icon)
        holder.chevron.visibility = View.GONE
        holder.dateText.text = Utils.formatTime(data.dtTxt)
        holder.weatherText.text = data.weather.main
        holder.tempText.text = activity.getString(R.string.temp_format, data.main.temp.toInt())
        setTempIconColor(holder.tempIcon, data.main.temp)
        holder.windText.text =
            activity.getString(R.string.wind_text_format_mph, data.wind.speed.toInt())
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
                tempIcon.contentDescription = activity.getString(R.string.blue_thermostat_icon)
                tempIcon.setColorFilter(
                    ContextCompat.getColor(
                        activity, R.color.md_theme_light_primary
                    )
                )
            }
            temp >= 85.0 -> {
                tempIcon.contentDescription = activity.getString(R.string.red_thermostat_icon)
                val color = ContextCompat.getColor(activity, R.color.md_theme_light_error)
                tempIcon.setColorFilter(color)
            }
            else -> {
                tempIcon.contentDescription =
                    activity.getString(R.string.thermostat_icon_content_description)
                tempIcon.clearColorFilter()
            }
        }
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val chevron: ImageView = ItemView.findViewById(R.id.chevron)
        val weatherImage: ImageView = ItemView.findViewById(R.id.weatherImage)
        val dateText: TextView = ItemView.findViewById(R.id.dateText)
        val weatherText: TextView = ItemView.findViewById(R.id.weatherText)
        val tempIcon: ImageView = ItemView.findViewById(R.id.tempIcon)
        val tempText: TextView = ItemView.findViewById(R.id.tempText)
        val windText: TextView = ItemView.findViewById(R.id.windText)
    }
}
