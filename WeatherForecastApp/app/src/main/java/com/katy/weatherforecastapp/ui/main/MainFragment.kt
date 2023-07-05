package com.katy.weatherforecastapp.ui.main

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.katy.weatherforecastapp.App
import com.katy.weatherforecastapp.R
import com.katy.weatherforecastapp.adapter.DayForecastAdapter
import com.katy.weatherforecastapp.model.Location
import com.katy.weatherforecastapp.model.WeatherData
import com.katy.weatherforecastapp.network.NetworkCapabilities
import com.katy.weatherforecastapp.ui.dialog.ZipCodeDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private val repository by lazy { App.repository }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(!NetworkCapabilities().hasInternetAccess(requireContext())){
            checkForCachedLocation()
        } else if (!viewModel.location.isInitialized){
            promptForZipCode()
        }
        setUpObservers()
    }

    private fun showNoInternetNoDataDialog() {
        TODO("Not yet implemented")
    }

    private fun setUpObservers() {
        viewModel.location.observe(viewLifecycleOwner){
            addLocationToDatabase(it)
            setUpView(it)
        }
    }

    private fun addLocationToDatabase(location:Location) {
        GlobalScope.launch(Dispatchers.IO) {
            repository.addLocation(location)
        }
    }

    private fun checkForCachedLocation() {
        GlobalScope.launch(Dispatchers.IO) {
            repository.getLocation()?.let{
                viewModel.location.postValue(it)
                Log.d("DEBUG", "Cached Location:" + it.locationName)
            }?: showNoInternetNoDataDialog()
        }
    }

    private fun promptForZipCode() {
        activity?.supportFragmentManager?.let { ZipCodeDialogFragment(viewModel).show(it, "") }
    }

    private fun setUpView(location: Location) {
        val locationTitle = view?.findViewById<TextView>(R.id.locationText)
        locationTitle?.text = location.locationName
        setUpForecastRecycler(location.lat, location.lon)
    }

    private fun setUpForecastRecycler(latitude: String, longitude: String){
        App.openWeatherApi.getFiveDayForecast(latitude,longitude){ weatherDataList: List<List<WeatherData>> ->
            val forecastRecyclerView = view?.findViewById<RecyclerView>(R.id.forecastRecyclerView)
            forecastRecyclerView?.layoutManager = LinearLayoutManager(activity)
            forecastRecyclerView?.adapter = activity?.let { DayForecastAdapter(weatherDataList, it) }
        }

    }



}