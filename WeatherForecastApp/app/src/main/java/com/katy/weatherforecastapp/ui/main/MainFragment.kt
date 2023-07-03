package com.katy.weatherforecastapp.ui.main

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.katy.weatherforecastapp.App
import com.katy.weatherforecastapp.R
import com.katy.weatherforecastapp.adapters.DayForecastAdapter
import com.katy.weatherforecastapp.model.LatLonResponse
import com.katy.weatherforecastapp.model.WeatherData
import com.katy.weatherforecastapp.ui.dialog.ZipCodeDialogFragment

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
        if (!viewModel.latLonResponse.isInitialized){
            promptForZipCode()
        }
        viewModel.latLonResponse.observe(viewLifecycleOwner){
            setUpView(it)
        }
    }

    private fun promptForZipCode() {
        activity?.supportFragmentManager?.let { ZipCodeDialogFragment(viewModel).show(it, "") }
    }

    private fun setUpView(latLonResponse: LatLonResponse) {
        val locationTitle = view?.findViewById<TextView>(R.id.locationText)
        locationTitle?.text = latLonResponse.locationName
        setUpForecastRecycler(latLonResponse.lat, latLonResponse.lon)
    }

    private fun setUpForecastRecycler(latitude: String, longitude: String){
        App.openWeatherApi.getFiveDayForecast(latitude,longitude){ weatherDataList: List<List<WeatherData>> ->
            val forecastRecyclerView = view?.findViewById<RecyclerView>(R.id.forecastRecyclerView)
            forecastRecyclerView?.layoutManager = LinearLayoutManager(activity)
            forecastRecyclerView?.adapter = activity?.let { DayForecastAdapter(weatherDataList, it) }
        }

    }



}