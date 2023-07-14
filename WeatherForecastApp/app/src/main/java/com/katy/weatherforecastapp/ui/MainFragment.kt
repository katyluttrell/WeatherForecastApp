package com.katy.weatherforecastapp.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.katy.weatherforecastapp.R
import com.katy.weatherforecastapp.adapter.DayForecastAdapter
import com.katy.weatherforecastapp.databinding.FragmentMainBinding
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
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main, container, false
        )
        return binding.root
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
            viewModel.cleanDatabase()
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
        binding.locationText.text = location.locationName
        binding.editButton.visibility = View.VISIBLE
        binding.editButton.setOnClickListener {
            promptForZipCode()
        }
    }

    private fun setUpForecastRecycler(weatherDataList: List<List<WeatherData>>) {
        val forecastRecyclerView = binding.forecastRecyclerView
        forecastRecyclerView.layoutManager = LinearLayoutManager(activity)
        forecastRecyclerView.adapter = DayForecastAdapter(weatherDataList)
    }

}