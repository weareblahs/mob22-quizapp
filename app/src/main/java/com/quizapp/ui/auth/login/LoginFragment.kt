package com.quizapp.ui.auth.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.quizapp.R
import com.quizapp.databinding.FragmentLoginBinding
import com.quizapp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment() {
    private lateinit var binding: FragmentLoginBinding
    override val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)


        binding.btnGoogleSignIn.setOnClickListener {
            viewModel.loginWithGoogle(requireContext())
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        lifecycleScope.launch {
            viewModel.loginInfo.collect {
                if(it.role.isNullOrBlank() && it.role != "") {
                    Log.d("debugging", "no role")
                } else {
                   if(it.role == "") {
                       val dir = LoginFragmentDirections.actionLoginFragmentToRoleSelectionFragment()
                       findNavController().navigate(dir)
                   } else {
                       when(it.role) {
                           "teacher" -> findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToTeacherDashboard())
                           "student" -> findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToStudentDashboard())
                       }
                   }
                }
            }
        }
    }
}