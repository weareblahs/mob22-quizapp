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
// this file uses Fragment instead of BaseFragment due to access required for onResume lifecycle
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    private var isResumedFromBackStack = false
    private var hasBeenCreated = false

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
                        "teacher" -> findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToTeacherDashboard())
                        "student" -> findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToStudentDashboard())
                        "" -> findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRoleSelectionFragment())
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
        Log.d("debugging", "isResumedFromBackStack: ${isResumedFromBackStack}, hasBeenCreated: ${hasBeenCreated}")
        if (hasBeenCreated && isResumedFromBackStack) {
            requireActivity().finish()
        }
        if (!hasBeenCreated) {
            hasBeenCreated = true
        } else {
            isResumedFromBackStack = true
        }
    }
}