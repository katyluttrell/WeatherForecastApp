package com.katy.weatherforecastapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.katy.weatherforecastapp.R
import com.katy.weatherforecastapp.adapter.TimeForecastAdapter
import com.katy.weatherforecastapp.model.WeatherData
import com.katy.weatherforecastapp.network.LinkFactory
import com.katy.weatherforecastapp.viewmodel.SingleDayForecastViewModel

class SingleDayForecastFragment : Fragment() {

    private val args: SingleDayForecastFragmentArgs by navArgs()
    private val viewModel: SingleDayForecastViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_single_day_forecast, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dayWeatherData = args.weatherDataList.asList()
        if (!dayWeatherData.isNullOrEmpty()) {
            populateCurrentWeatherData(dayWeatherData[0])
            if (dayWeatherData.size > 1) {
                setUpLaterTimesRecycler(dayWeatherData.subList(1, dayWeatherData.size))
            }
        }
    }

    private fun setUpLaterTimesRecycler(subList: List<WeatherData>) {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.laterTimesRecycler)
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        recyclerView?.adapter = TimeForecastAdapter(subList)
    }

    private fun populateCurrentWeatherData(weatherNow: WeatherData) {
        view?.findViewById<ImageView>(R.id.nowWeatherImage)
            ?.let { loadImage(weatherNow.weather.icon, it) }
        view?.findViewById<TextView>(R.id.nowWeatherName)?.text = weatherNow.weather.main
        view?.findViewById<TextView>(R.id.nowWeatherDescription)?.text =
            weatherNow.weather.description
        view?.findViewById<TextView>(R.id.humidity)?.text =
            context?.getString(R.string.humidity, weatherNow.main.humidity)
        view?.findViewById<TextView>(R.id.tempHighLow)?.text = context?.getString(
            R.string.high_low_temp,
            weatherNow.main.tempMax.toInt().toString(),
            weatherNow.main.tempMin.toInt().toString()
        )
        view?.findViewById<TextView>(R.id.wind)?.text = context?.getString(
            R.string.wind_desc,
            weatherNow.wind.speed.toInt().toString(),
            weatherNow.wind.gust.toInt().toString()
        )
    }

    private fun loadImage(icon: String, weatherImage: ImageView) {
        val url = LinkFactory().openWeatherIconLink(icon)
        weatherImage.load(url) {
            size(350, 350)
            placeholder(R.drawable.baseline_sync)
            error(R.drawable.baseline_sync_problem_24)
        }
    }

}