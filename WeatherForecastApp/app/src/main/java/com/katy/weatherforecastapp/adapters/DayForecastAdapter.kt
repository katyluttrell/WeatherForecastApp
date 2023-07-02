package com.katy.weatherforecastapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.katy.weatherforecastapp.R
import com.katy.weatherforecastapp.model.WeatherData

class DayForecastAdapter(private val dataList: List<WeatherData>): RecyclerView.Adapter<DayForecastAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.day_forecast_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.dateText.text = data.dtTxt
        holder.weatherText.text = data.weather[0].main
        holder.tempText.text = data.main.temp.toString()
        holder.windText.text = data.wind.speed.toString()
        holder.itemView.setOnClickListener { onClick(holder, data) }
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val weatherImage: ImageView = ItemView.findViewById(R.id.weatherImage)
        val dateText: TextView = ItemView.findViewById(R.id.dateText)
        val weatherText: TextView = ItemView.findViewById(R.id.weatherText)
        val tempText: TextView = ItemView.findViewById(R.id.tempText)
        val windText: TextView = ItemView.findViewById(R.id.windText)
    }

    fun onClick(holder: ViewHolder, data: WeatherData){
        //TODO: new fragment with additional weather info
    }


}

