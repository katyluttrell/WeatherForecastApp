package com.katy.weatherforecastapp.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputLayout
import com.katy.weatherforecastapp.App
import com.katy.weatherforecastapp.R
import com.katy.weatherforecastapp.ui.main.MainViewModel

class ZipCodeDialogFragment(private val viewModel: MainViewModel) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.zip_code_dialog, null)
        setButtonBehavior(dialog, dialogView)
        dialog.setContentView(dialogView)
        dialog.setCancelable(false)
        return dialog
    }

    private fun setButtonBehavior(dialog: Dialog, dialogView: View) {
        val button = dialogView.findViewById<Button>(R.id.okButton)
        button?.setOnClickListener {
            val editText = dialogView.findViewById<TextInputLayout>(R.id.zipCodeTextField)
            editText?.let{
                val entry = it.editText?.text.toString()
                if(isValidZipCode(entry)){
                    App.openWeatherApi.getLatLong(entry, { response ->
                        viewModel.location.postValue(response)
                        dialog.dismiss()
                    }, {
                        displayValidationError(editText)
                    })
                } else{
                    displayValidationError(editText)
                }
            }
        }
    }

    private fun displayValidationError(editText: TextInputLayout){
        editText.error = "Invalid Zip Code"
    }

    private fun isValidZipCode(zipCode:String?):Boolean = !zipCode.isNullOrEmpty() && zipCode.length == 5 && zipCode.isDigitsOnly()


}