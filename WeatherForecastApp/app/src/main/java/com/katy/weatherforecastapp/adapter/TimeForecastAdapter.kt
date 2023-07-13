package com.katy.weatherforecastapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.katy.weatherforecastapp.R
import com.katy.weatherforecastapp.model.WeatherData
import com.katy.weatherforecastapp.util.RecyclerViewAdapterUtils

class TimeForecastAdapter(private val dataList: List<WeatherData>) :
    RecyclerView.Adapter<TimeForecastAdapter.ViewHolder>() {

    val utils = RecyclerViewAdapterUtils()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.day_forecast_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val data = dataList[position]
        utils.setWeatherImage(holder.weatherImage, data.weather.icon)
        holder.chevron.visibility = View.GONE
        holder.dateText.text = utils.formatTime(data.dtTxt)
        holder.weatherText.text = data.weather.main
        holder.tempText.text = context.getString(R.string.temp_format, data.main.temp.toInt())
        val tempIconColor = utils.getTempIconColor(data.main.temp)
        holder.tempIcon.clearColorFilter()
        tempIconColor?.let {holder.tempIcon.setColorFilter( ContextCompat.getColor(context, it))}
        holder.windText.text =
            context.getString(R.string.wind_text_format_mph, data.wind.speed.toInt())
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
