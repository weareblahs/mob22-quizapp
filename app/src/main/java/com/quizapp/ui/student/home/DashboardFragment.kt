package com.quizapp.ui.student.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.quizapp.R
import com.quizapp.data.model.Quiz
import com.quizapp.databinding.FragmentStudentDashboardBinding
import com.quizapp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardFragment : BaseFragment() {
    override val viewModel: DashboardViewModel by viewModels()
    private lateinit var binding: FragmentStudentDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)

        parentFragmentManager.setFragmentResultListener("requests", viewLifecycleOwner) { _, bundle ->
            val shouldReset = bundle.getBoolean("should_reset", false)
            if (shouldReset) {
                // Reset the quiz info in ViewModel
                viewModel.resetQuiz()
                // Also clear the input field
                binding.etCodeInput.text?.clear()
            }

        }
        binding.btnContinue.setOnClickListener {
            val code = binding.etCodeInput.text.toString()
            viewModel.checkQuiz(code)
        }
        Glide.with(binding.profilePicture).load(viewModel.getProfileUrl()).into(binding.profilePicture) // get profile picture from google account

        // when tapped on profile photo, show confirmation, then log out
        binding.profilePicture.setOnClickListener {
            showDialog(
                getString(R.string.log_out),
                getString(R.string.log_out_confirmation),
                getString(R.string.log_out),
                ::logout,
                false // since the MutableStateFlow is updated and the snackbar is handled by setupViewModelObserver, the snackbar option in this dialog is set to false
            )
        }

    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()
        lifecycleScope.launch {
            viewModel.shouldNavigate.collect {
                val base = viewModel.quizInfo.value
                if(base != Quiz() && viewModel.shouldNavigate.value) {
                    Log.d("debugging", "valid quiz")
                    val dir = DashboardFragmentDirections.actionStudentDashboardToStartQuizFragment(binding.etCodeInput.text.toString())
                    findNavController().navigate(dir)
                    viewModel.resetNavigationFlag()
                } else {
                    Log.d("debugging", "invalid quiz")
                }
            }
        }
        lifecycleScope.launch {
            viewModel.errorMsg.collect {
                if(viewModel.errorMsg.value.isNotEmpty()) showError(viewModel.errorMsg.value)
            }
        }
        lifecycleScope.launch {
            viewModel.logout.collect {
                if(viewModel.logout.value) {
                    // if logout is set to true then redirect back to login page
                    findNavController().navigate(DashboardFragmentDirections.actionStudentDashboardToLoginFragment(), NavOptions.Builder().setPopUpTo(findNavController().graph.startDestinationId, true).build())
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        viewModel.resetQuiz() // This now resets both the quiz data and navigation flag
    }

    private fun logout() {
        viewModel.logout()
    }
}