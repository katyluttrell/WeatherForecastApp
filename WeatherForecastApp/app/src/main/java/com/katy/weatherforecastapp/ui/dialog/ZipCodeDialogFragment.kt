package com.katy.weatherforecastapp.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.isDigitsOnly
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.katy.weatherforecastapp.R
import com.katy.weatherforecastapp.databinding.ZipCodeDialogBinding
import com.katy.weatherforecastapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ZipCodeDialogFragment : DialogFragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: ZipCodeDialogBinding
    private var zipcodeAttempted = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.zip_code_dialog, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
        setObservers()
        setButtonBehavior()
        if(savedInstanceState != null){
            binding.zipCodeTextField.editText?.setText(savedInstanceState.getString(ZIPCODE_TEXT_KEY))
            binding.zipCodeTextField.error = savedInstanceState.getString(ENTRY_ERROR_KEY)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ZIPCODE_TEXT_KEY, binding.zipCodeTextField.editText?.text.toString())
        outState.putString(ENTRY_ERROR_KEY, binding.zipCodeTextField.error.toString())
    }

    private fun setObservers() {
        viewModel.location.observe(this) {
            if (zipcodeAttempted) {
                findNavController().navigateUp()
            }
        }
        viewModel.zipcodeValidationError.observe(this) {
            if (it) {
                displayValidationError(binding.zipCodeTextField)
            }
        }

    }

    private fun setButtonBehavior() {
        binding.okButton.setOnClickListener {
            val textField = binding.zipCodeTextField
            val entry = textField.editText?.text.toString()
            if (isValidZipCode(entry)) {
                zipcodeAttempted = true
                binding.zipCodeTextField.error = ""
                viewModel.startObservingLocationData(entry)
            } else {
                displayValidationError(textField)
            }
        }
    }

    private fun displayValidationError(editText: TextInputLayout) {
        editText.error = "Invalid Zip Code"
    }

    private fun isValidZipCode(zipCode: String?): Boolean =
        !zipCode.isNullOrEmpty() && zipCode.length == 5 && zipCode.isDigitsOnly()


    companion object{
        const val ZIPCODE_TEXT_KEY = "ZipcodeTextKey"
        const val ENTRY_ERROR_KEY = "EntryErrorKey"
    }
}