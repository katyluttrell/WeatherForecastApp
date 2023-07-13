package com.katy.weatherforecastapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.katy.weatherforecastapp.R
import com.katy.weatherforecastapp.adapter.TimeForecastAdapter
import com.katy.weatherforecastapp.databinding.FragmentSingleDayForecastBinding
import com.katy.weatherforecastapp.model.WeatherData
import com.katy.weatherforecastapp.network.LinkFactory
import com.katy.weatherforecastapp.viewmodel.SingleDayForecastViewModel

class SingleDayForecastFragment : Fragment() {

    private val args: SingleDayForecastFragmentArgs by navArgs()
    private val viewModel: SingleDayForecastViewModel by viewModels()
    private lateinit var binding: FragmentSingleDayForecastBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_single_day_forecast, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dayWeatherData = args.weatherDataList.asList()
        if (dayWeatherData.isNotEmpty()) {
            binding.nowWeather = dayWeatherData[0]
            populateCurrentWeatherData(dayWeatherData[0])
            if (dayWeatherData.size > 1) {
                setUpLaterTimesRecycler(dayWeatherData.subList(1, dayWeatherData.size))
            }
        }
    }

    private fun setUpLaterTimesRecycler(subList: List<WeatherData>) {
        val recyclerView = binding.laterTimesRecycler
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = TimeForecastAdapter(subList)
    }

    private fun populateCurrentWeatherData(weatherNow: WeatherData) {
        loadImage(weatherNow.weather.icon, binding.nowWeatherImage)
        binding.humidity.text = context?.getString(R.string.humidity, weatherNow.main.humidity)
        binding.tempHighLow.text = context?.getString(
            R.string.high_low_temp,
            weatherNow.main.tempMax.toInt().toString(),
            weatherNow.main.tempMin.toInt().toString()
        )
        binding.wind.text = context?.getString(
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