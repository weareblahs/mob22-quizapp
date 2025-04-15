package com.quizapp.ui.base

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

abstract class BaseFragment: Fragment() {
    protected abstract val viewModel: BaseViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onFragmentResult()
        setupUiComponents(view)
        setupViewModelObserver()
    }

    protected open fun onFragmentResult() {

    }

    protected open fun setupViewModelObserver() {
        lifecycleScope.launch {
            viewModel.error.collect {
                showError(it)
            }
        }
    }

    protected open fun setupUiComponents(view: View){

    }

    private fun showError(msg: String) {
        Snackbar.make(requireView(), msg, Snackbar.LENGTH_LONG).apply {
            setBackgroundTint(
                ContextCompat.getColor (
                    requireContext(),
                    com.google.android.material.R.color.design_default_color_error
                )
            )
            setTextColor(
                ContextCompat.getColor(
                requireContext(),
                com.noteapp.R.color.white
            ))
        }.show()
    }

    // below is an implementation for a dialog handler for all fragments across the app
    fun showDialog(title: String, // passes title
                   message: String, // passes message
                   confirmText: String, // passes positive text (usually "Confirm" or "Sure")
                   function: () -> (Unit), // passes a function. do note that the function needs to be passed like "::function" and no parameters are allowed for this case
                   snackbar: Boolean, // specifies as boolean (true / false) if a snackbar should be shown after running function
                   snackbarMsg: String? = "" // specifies message shown in snackbar if snackbar is set to true
    ) {
        DialogUtils.showConfirmationDialog(
            context = requireContext(),
            title = title,
            message = message,
            positiveText = confirmText,
            negativeText = "Cancel"
        ) {
            function() // if confirmed, run function
            if(snackbar) {
                Snackbar.make(requireView(), snackbarMsg!!, Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}