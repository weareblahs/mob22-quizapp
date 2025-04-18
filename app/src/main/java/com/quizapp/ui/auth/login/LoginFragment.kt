package com.quizapp.ui.auth.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.quizapp.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    private var isResumedFromBackStack = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnGoogleSignIn.setOnClickListener {
            viewModel.loginWithGoogle(requireContext())
        }


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

    // exit app when back button tapped. since all the logic is in
    // the login fragment, not doing this will cause a "trap" that
    // will redirect back to the dashboard fragment everytime
    override fun onResume() {
        super.onResume()
        if (isResumedFromBackStack) {
            requireActivity().finish()
        }
        isResumedFromBackStack = true
    }
}