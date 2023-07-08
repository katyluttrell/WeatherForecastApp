package com.katy.weatherforecastapp.ui

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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.katy.weatherforecastapp.R
import com.katy.weatherforecastapp.adapter.DayForecastAdapter
import com.katy.weatherforecastapp.model.Location
import com.katy.weatherforecastapp.model.WeatherData
import com.katy.weatherforecastapp.network.NetworkUtils
import com.katy.weatherforecastapp.ui.dialog.AlertDialogFactory
import com.katy.weatherforecastapp.ui.dialog.MainViewDialog
import com.katy.weatherforecastapp.ui.dialog.OnOkCallback
import com.katy.weatherforecastapp.ui.dialog.ZipCodeDialogFragment
import com.katy.weatherforecastapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {

    @Inject
    lateinit var networkUtils: NetworkUtils
    @Inject
    lateinit var alertDialogFactory: AlertDialogFactory
    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.hasInternet = networkUtils.hasInternetAccess(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpObservers()
        viewModel.startNetworkOrCacheFetches()
    }

    private fun setUpObservers() {
        viewModel.location.observe(viewLifecycleOwner){
            setUpView(it)
        }
        viewModel.weatherDataList.observe(viewLifecycleOwner){
            setUpForecastRecycler(it)
        }
        viewModel.currentDialog.observe(viewLifecycleOwner){ event ->
            event?.let {
                showDialog(it)
                viewModel.currentDialog.postValue(null)
            }

        }
    }

    private fun showDialog(mainViewDialog: MainViewDialog) {
        when(mainViewDialog){
            MainViewDialog.ZipCodePrompt -> promptForZipCode()
            else -> alertDialogFactory.createDialog(
                AlertDialog.Builder(context),
                mainViewDialog, object :OnOkCallback{
                    override fun onOkPress() {
                        viewModel.currentDialog.postValue(null)
                    }
                }).show()
        }
    }

    private fun promptForZipCode() {
        activity?.supportFragmentManager?.let { ZipCodeDialogFragment(viewModel).show(it, "ZipCodeDialogFragment") }
    }

    private fun setUpView(location: Location) {
        val locationTitle = view?.findViewById<TextView>(R.id.locationText)
        locationTitle?.text = location.locationName
        val editLocationButton = view?.findViewById<FloatingActionButton>(R.id.editButton)
        editLocationButton?.visibility = View.VISIBLE
        editLocationButton?.setOnClickListener {
            viewModel.hasInternet = networkUtils.hasInternetAccess(requireContext())
            viewModel.editLocation()
        }
    }
    private fun setUpForecastRecycler(weatherDataList: List<List<WeatherData>>){
        val forecastRecyclerView = view?.findViewById<RecyclerView>(R.id.forecastRecyclerView)
        forecastRecyclerView?.layoutManager = LinearLayoutManager(activity)
        forecastRecyclerView?.adapter = activity?.let { DayForecastAdapter(weatherDataList, it) }
    }

}