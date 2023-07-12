package com.katy.weatherforecastapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.katy.weatherforecastapp.R
import com.katy.weatherforecastapp.model.WeatherData
import com.katy.weatherforecastapp.ui.MainFragmentDirections
import com.katy.weatherforecastapp.util.RecyclerViewAdapterUtils
import com.katy.weatherforecastapp.util.capitalize

class DayForecastAdapter(
    private val dataList: List<List<WeatherData>>
) : RecyclerView.Adapter<DayForecastAdapter.ViewHolder>() {

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
        val day = dataList[position]
        val data = utils.getMiddleOrFirstTime(day)
        data?.let {
            utils.setWeatherImage(holder.weatherImage, it.weather.icon)
            holder.dateText.text = utils.formatDate(it.dtTxt)?.let { str -> context.getString(str) }
                ?: it.dtTxt.dayOfWeek.toString().capitalize()
            holder.weatherText.text = it.weather.main
            holder.tempText.text = context.getString(R.string.temp_format, it.main.temp.toInt())
            val tempIconColor = utils.getTempIconColor(data.main.temp)
            holder.tempIcon.clearColorFilter()
            tempIconColor?.let {color -> holder.tempIcon.setColorFilter( ContextCompat.getColor(context, color))}
            holder.windText.text =
                context.getString(R.string.wind_text_format_mph, it.wind.speed.toInt())
        }
        holder.itemView.setOnClickListener { onClick(day, holder.itemView) }
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val weatherImage: ImageView = ItemView.findViewById(R.id.weatherImage)
        val dateText: TextView = ItemView.findViewById(R.id.dateText)
        val weatherText: TextView = ItemView.findViewById(R.id.weatherText)
        val tempIcon: ImageView = ItemView.findViewById(R.id.tempIcon)
        val tempText: TextView = ItemView.findViewById(R.id.tempText)
        val windText: TextView = ItemView.findViewById(R.id.windText)
    }

    fun onClick(dayData: List<WeatherData>, view: View) {
        val action =
            MainFragmentDirections.actionMainFragmentToSingleDayForecastFragment(dayData.toTypedArray())
        view.findNavController().navigate(action)
    }


}

