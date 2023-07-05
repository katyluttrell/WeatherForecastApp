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

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private var hasInternet: Boolean? = null
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
        if(hasInternet != true){
            checkForCachedLocation()
        } else if (!viewModel.location.isInitialized){
            promptForZipCode()
        }
        setUpObservers()
    }
    private fun showNoInternetOldDataDialog() {
        if(!viewModel.noInternetAlertShown){
            AlertDialog.Builder(activity)
                .setTitle("No Internet")
                .setMessage("Data shown is from last time app was used with internet access.")
                .setNeutralButton("OK"){ dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
            viewModel.noInternetAlertShown = true
        }
    }

    private fun showNoInternetNoDataDialog() {
        if(!viewModel.noInternetAlertShown) {
            AlertDialog.Builder(activity)
                .setTitle("No Internet Access or Saved Data")
                .setMessage("Please try again later.")
                .setNeutralButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
            viewModel.noInternetAlertShown = true
        }
    }

    private fun setUpObservers() {
        viewModel.location.observe(viewLifecycleOwner){
            setUpView(it)
            if(hasInternet == true) {
                addLocationToDatabase(it)
                fetchFiveDayForecast(it)
            }else{
                checkForCachedWeatherData()
            }
        }
        viewModel.weatherDataList.observe(viewLifecycleOwner){
            if(hasInternet == true) {
                addWeatherDataToDatabase(it)
            }
            setUpForecastRecycler(it)
        }
    }

    private fun checkForCachedWeatherData() {
        GlobalScope.launch(Dispatchers.IO) {
            val list = repository.getFiveDayForecastList()
                if(!list.isNullOrEmpty()){
                viewModel.weatherDataList.postValue(list)
                GlobalScope.launch(Dispatchers.Main) {
                    showNoInternetOldDataDialog()
                }
            }else{ GlobalScope.launch(Dispatchers.Main) {showNoWeatherDataDialog()}}
        }
    }

    private fun showNoWeatherDataDialog() {
        if(!viewModel.noInternetAlertShown) {
            AlertDialog.Builder(activity)
                .setTitle("No Weather Data for This Location")
                .setMessage("Please try again later with internet access.")
                .setNeutralButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
            viewModel.noInternetAlertShown = true
        }
    }

    private fun addWeatherDataToDatabase(data: List<List<WeatherData>>) {
        GlobalScope.launch(Dispatchers.IO) {
            repository.deleteAllWeatherData()
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
            }?: GlobalScope.launch(Dispatchers.Main) { showNoInternetNoDataDialog() }
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