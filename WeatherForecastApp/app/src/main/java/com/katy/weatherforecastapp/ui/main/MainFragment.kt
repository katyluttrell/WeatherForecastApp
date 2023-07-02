package com.katy.weatherforecastapp.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.katy.weatherforecastapp.App
import com.katy.weatherforecastapp.R
import com.katy.weatherforecastapp.adapters.DayForecastAdapter
import com.katy.weatherforecastapp.model.FiveDayForecast
import retrofit2.Response

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpForecastRecycler()
    }

    private fun setUpForecastRecycler(){
        App.openWeatherApi.getFiveDayForecast(0.0,0.0){ response: Response<FiveDayForecast> ->
            val forecastRecyclerView = view?.findViewById<RecyclerView>(R.id.forecastRecyclerView)
            forecastRecyclerView?.layoutManager = LinearLayoutManager(activity)
            forecastRecyclerView?.adapter = response.body()?.list?.let { DayForecastAdapter(it) }
        }

    }



}