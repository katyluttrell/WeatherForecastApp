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
import kotlin.properties.Delegates

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private var hasInternet = false
    private val repository by lazy { App.repository }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hasInternet = NetworkCapabilities().hasInternetAccess(requireContext())
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
        if(!hasInternet){
            checkForCachedLocation()
        } else if (!viewModel.location.isInitialized){
            promptForZipCode()
        }
        setUpObservers()
    }

    private fun showNoInternetNoDataDialog() {
        //TODO("Not yet implemented")
    }

    private fun setUpObservers() {
        viewModel.location.observe(viewLifecycleOwner){
            addLocationToDatabase(it)
            setUpView(it)
            if(hasInternet) {
                fetchFiveDayForecast(it)
            }else{
                checkForCachedWeatherData()
            }
        }
        viewModel.weatherDataList.observe(viewLifecycleOwner){
            addWeatherDataToDatabase(it)
            setUpForecastRecycler(it)
        }
    }

    private fun checkForCachedWeatherData() {
        GlobalScope.launch(Dispatchers.IO) {
            repository.getFiveDayForecastList()?.let{
                viewModel.weatherDataList.postValue(it)
            }?: showNoWeatherDataDialog()
        }
    }

    private fun showNoWeatherDataDialog() {
        TODO("Not yet implemented")
    }

    private fun addWeatherDataToDatabase(data: List<List<WeatherData>>) {
        GlobalScope.launch(Dispatchers.IO) {
            repository.addFiveDayForecastList(data)
        }
    }

    private fun fetchFiveDayForecast(location: Location) {
        App.openWeatherApi.getFiveDayForecast(location.lat, location.lon){
            viewModel.weatherDataList.postValue(it)
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
            }?: showNoInternetNoDataDialog()
        }
    }

    private fun promptForZipCode() {
        activity?.supportFragmentManager?.let { ZipCodeDialogFragment(viewModel).show(it, "") }
    }

    private fun setUpView(location: Location) {
        val locationTitle = view?.findViewById<TextView>(R.id.locationText)
        locationTitle?.text = location.locationName
    }
    private fun setUpForecastRecycler(weatherDataList: List<List<WeatherData>>){
        val forecastRecyclerView = view?.findViewById<RecyclerView>(R.id.forecastRecyclerView)
        forecastRecyclerView?.layoutManager = LinearLayoutManager(activity)
        forecastRecyclerView?.adapter = activity?.let { DayForecastAdapter(weatherDataList, it) }
    }

}