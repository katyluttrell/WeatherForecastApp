package com.katy.weatherforecastapp.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputLayout
import com.katy.weatherforecastapp.R
import com.katy.weatherforecastapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ZipCodeDialogFragment(val viewModel: MainViewModel) : DialogFragment() {

    lateinit var dialogView: View
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        dialogView = layoutInflater.inflate(R.layout.zip_code_dialog, null)
        builder.setView(dialogView)
            .setCancelable(false)
        return builder.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return dialogView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        setButtonBehavior()
    }

    private fun setObservers() {
        viewModel.location.observe(this) {
            dialog?.dismiss()
        }
        viewModel.zipcodeValidationError.observe(this) {
            if (it) {
                view?.findViewById<TextInputLayout>(R.id.zipCodeTextField)
                    ?.let { it1 -> displayValidationError(it1) }
            }
        }

    }

    private fun setButtonBehavior() {
        val button = view?.findViewById<Button>(R.id.okButton)
        button?.setOnClickListener {
            val editText = view?.findViewById<TextInputLayout>(R.id.zipCodeTextField)
            editText?.let {
                val entry = it.editText?.text.toString()
                if (isValidZipCode(entry)) {
                    viewModel.startObservingLocationData(entry)
                } else {
                    displayValidationError(editText)
                }
            }
        }
    }

    private fun displayValidationError(editText: TextInputLayout) {
        editText.error = "Invalid Zip Code"
    }

    private fun isValidZipCode(zipCode: String?): Boolean =
        !zipCode.isNullOrEmpty() && zipCode.length == 5 && zipCode.isDigitsOnly()


}