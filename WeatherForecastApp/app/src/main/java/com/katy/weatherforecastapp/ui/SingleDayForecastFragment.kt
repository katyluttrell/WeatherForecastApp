package com.katy.weatherforecastapp.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.katy.weatherforecastapp.R
import com.katy.weatherforecastapp.adapter.TimeForecastAdapter
import com.katy.weatherforecastapp.model.WeatherData
import com.katy.weatherforecastapp.network.LinkFactory
import com.katy.weatherforecastapp.viewmodel.SingleDayForecastViewModel

class SingleDayForecastFragment : Fragment() {

    companion object {
        const val WEATHER_DATA_KEY = "DayWeatherData"
        fun newInstance(dayWeatherData: List<WeatherData>): SingleDayForecastFragment {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(!dayWeatherData.isNullOrEmpty()) {
            populateCurrentWeatherData(dayWeatherData!![0])
            if(dayWeatherData!!.size > 1){
                setUpLaterTimesRecycler(dayWeatherData!!.subList(1, dayWeatherData!!.size))
            }
        }
    }

    private fun setUpLaterTimesRecycler(subList: List<WeatherData>) {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.laterTimesRecycler)
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        recyclerView?.adapter = activity?.let { TimeForecastAdapter(subList, it) }

    }

    private fun populateCurrentWeatherData(weatherNow: WeatherData) {
        view?.findViewById<ImageView>(R.id.nowWeatherImage)?.let { loadImage(weatherNow.weather.icon, it) }
        view?.findViewById<TextView>(R.id.nowWeatherName)?.text = weatherNow.weather.main
        view?.findViewById<TextView>(R.id.nowWeatherDescription)?.text = weatherNow.weather.description
        view?.findViewById<TextView>(R.id.humidity)?.text = context?.getString(R.string.humidity, weatherNow.main.humidity)
        view?.findViewById<TextView>(R.id.tempHighLow)?.text = context?.getString(R.string.high_low_temp, weatherNow.main.tempMax.toInt().toString(), weatherNow.main.tempMin.toInt().toString())
        view?.findViewById<TextView>(R.id.wind)?.text = context?.getString(R.string.wind_desc, weatherNow.wind.speed.toInt().toString(), weatherNow.wind.gust.toInt().toString())
    }

    private fun loadImage(icon:String, weatherImage: ImageView){
        val url = LinkFactory().openWeatherIconLink(icon)
        weatherImage.load(url) {
            size(350, 350)
            placeholder(R.drawable.baseline_sync)
            error(R.drawable.baseline_sync_problem_24)
        }
    }

}