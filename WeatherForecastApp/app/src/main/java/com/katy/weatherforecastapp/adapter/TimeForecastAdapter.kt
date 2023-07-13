package com.katy.weatherforecastapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.katy.weatherforecastapp.R
import com.katy.weatherforecastapp.databinding.DayForecastCardBinding
import com.katy.weatherforecastapp.model.WeatherData
import com.katy.weatherforecastapp.util.RecyclerViewAdapterUtils

class TimeForecastAdapter(private val dataList: List<WeatherData>) :
    RecyclerView.Adapter<TimeForecastAdapter.ViewHolder>() {

    private val utils = RecyclerViewAdapterUtils()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            DayForecastCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val data = dataList[position]
        holder.binding.chevron.visibility = View.GONE
        utils.setWeatherImage(holder.binding.weatherImage, data.weather.icon)
        holder.binding.dateText.text = utils.formatTime(data.dtTxt)
        holder.binding.weatherText.text = data.weather.main
        holder.binding.tempText.text =
            context.getString(R.string.temp_format, data.main.temp.toInt())
        val tempIconColor = utils.getTempIconColor(data.main.temp)
        holder.binding.tempIcon.clearColorFilter()
        tempIconColor?.let {
            holder.binding.tempIcon.setColorFilter(
                ContextCompat.getColor(
                    context,
                    it
                )
            )
        }
        holder.binding.windText.text =
            context.getString(R.string.wind_text_format_mph, data.wind.speed.toInt())
    }

    class ViewHolder(val binding: DayForecastCardBinding) : RecyclerView.ViewHolder(binding.root)
}
