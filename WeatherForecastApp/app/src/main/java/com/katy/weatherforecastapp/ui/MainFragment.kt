package com.katy.weatherforecastapp.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.katy.weatherforecastapp.R
import com.katy.weatherforecastapp.adapter.DayForecastAdapter
import com.katy.weatherforecastapp.model.Location
import com.katy.weatherforecastapp.model.WeatherData
import com.katy.weatherforecastapp.ui.dialog.AlertDialogFactory
import com.katy.weatherforecastapp.ui.dialog.MainViewDialog
import com.katy.weatherforecastapp.ui.dialog.OnOkCallback
import com.katy.weatherforecastapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {

    @Inject
    lateinit var alertDialogFactory: AlertDialogFactory

    private val viewModel: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpObservers()
        if (!viewModel.location.isInitialized && findNavController().currentDestination?.id != R.id.zipCodeDialogFragment) {
            promptForZipCode()
        }
    }

    private fun setUpObservers() {
        viewModel.location.observe(viewLifecycleOwner) {
            setUpView(it)
        }
        viewModel.weatherDataList.observe(viewLifecycleOwner) {
            setUpForecastRecycler(it)
        }
        viewModel.currentDialog.observe(viewLifecycleOwner) { dialogType ->
            dialogType?.let {
                showDialog(it)
            }

        }
    }

    private fun showDialog(mainViewDialog: MainViewDialog) {
        alertDialogFactory.createDialog(
            AlertDialog.Builder(context),
            mainViewDialog, object : OnOkCallback {
                override fun onOkPress() {
                    viewModel.currentDialog.postValue(null)
                }
            }).show()
    }

    private fun promptForZipCode() {
        findNavController().navigate(R.id.action_mainFragment_to_zipCodeDialogFragment)
    }

    private fun setUpView(location: Location) {
        val locationTitle = view?.findViewById<TextView>(R.id.locationText)
        locationTitle?.text = location.locationName
        val editLocationButton = view?.findViewById<FloatingActionButton>(R.id.editButton)
        editLocationButton?.visibility = View.VISIBLE
        editLocationButton?.setOnClickListener {
            promptForZipCode()
        }
    }

    private fun setUpForecastRecycler(weatherDataList: List<List<WeatherData>>) {
        val forecastRecyclerView = view?.findViewById<RecyclerView>(R.id.forecastRecyclerView)
        forecastRecyclerView?.layoutManager = LinearLayoutManager(activity)
        forecastRecyclerView?.adapter = DayForecastAdapter(weatherDataList)
    }

}