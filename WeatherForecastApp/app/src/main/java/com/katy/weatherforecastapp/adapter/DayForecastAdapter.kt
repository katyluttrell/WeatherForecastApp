package com.katy.weatherforecastapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.katy.weatherforecastapp.R
import com.katy.weatherforecastapp.databinding.DayForecastCardBinding
import com.katy.weatherforecastapp.model.WeatherData
import com.katy.weatherforecastapp.ui.MainFragmentDirections
import com.katy.weatherforecastapp.util.RecyclerViewAdapterUtils
import com.katy.weatherforecastapp.util.capitalize

class DayForecastAdapter(
    private val dataList: List<List<WeatherData>>
) : RecyclerView.Adapter<DayForecastAdapter.ViewHolder>() {

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
        val day = dataList[position]
        val data = utils.getMiddleOrFirstTime(day)
        data?.let {
            utils.setWeatherImage(holder.binding.weatherImage, it.weather.icon)
            holder.binding.dateText.text =
                utils.formatDate(it.dtTxt)?.let { str -> context.getString(str) }
                    ?: it.dtTxt.dayOfWeek.toString().capitalize()
            holder.binding.tempText.text =
                context.getString(R.string.temp_format, it.main.temp.toInt())
            val tempIconColor = utils.getTempIconColor(data.main.temp)
            holder.binding.tempIcon.clearColorFilter()
            tempIconColor?.let { color ->
                holder.binding.tempIcon.setColorFilter(
                    ContextCompat.getColor(
                        context,
                        color
                    )
                )
            }
            holder.binding.windText.text =
                context.getString(R.string.wind_text_format_mph, it.wind.speed.toInt())
        }
        holder.itemView.setOnClickListener { onClick(day, holder.itemView) }
    }

    class ViewHolder(val binding: DayForecastCardBinding) : RecyclerView.ViewHolder(binding.root)

    fun onClick(dayData: List<WeatherData>, view: View) {
        val action =
            MainFragmentDirections.actionMainFragmentToSingleDayForecastFragment(dayData.toTypedArray())
        view.findNavController().navigate(action)
    }
}
