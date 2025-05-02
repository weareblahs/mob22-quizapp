package com.quizapp.ui.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.quizapp.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
// this file uses Fragment instead of BaseFragment due to access required for onResume lifecycle
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

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
                if(it.isLogin) {
                    when(it.role) {
                        "teacher" -> findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToTeacherDashboard(), NavOptions.Builder().setPopUpTo(findNavController().graph.startDestinationId, true).build())
                        "student" -> findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToStudentDashboard(), NavOptions.Builder().setPopUpTo(findNavController().graph.startDestinationId, true).build())
                        else -> findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRoleSelectionFragment(), NavOptions.Builder().setPopUpTo(findNavController().graph.startDestinationId, true).build())
                    }
                }
            }
        }
    }

}