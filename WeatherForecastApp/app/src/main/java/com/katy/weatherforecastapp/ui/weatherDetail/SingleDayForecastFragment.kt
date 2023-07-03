package com.katy.weatherforecastapp.ui.weatherDetail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.katy.weatherforecastapp.R
import com.katy.weatherforecastapp.model.Weather
import com.katy.weatherforecastapp.model.WeatherData

class SingleDayForecastFragment : Fragment() {

    companion object {
        const val WEATHER_DATA_KEY = "DayWeatherData"
        fun newInstance(dayWeatherData: List<WeatherData>):SingleDayForecastFragment{
            val fragment = SingleDayForecastFragment()
            val args = Bundle()
            args.putParcelableArrayList(WEATHER_DATA_KEY, ArrayList<WeatherData>(dayWeatherData))
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var viewModel: SingleDayForecastViewModel
    private var dayWeatherData: List<WeatherData>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dayWeatherData = arguments?.getParcelableArrayList<WeatherData>(WEATHER_DATA_KEY)?.toList()
        return inflater.inflate(R.layout.fragment_single_day_forecast, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SingleDayForecastViewModel::class.java)
        // TODO: Use the ViewModel
    }

}