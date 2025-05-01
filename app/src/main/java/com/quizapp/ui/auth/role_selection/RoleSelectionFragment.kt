package com.quizapp.ui.auth.role_selection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.quizapp.R
import com.quizapp.databinding.FragmentRoleSelectionBinding
import com.quizapp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoleSelectionFragment : BaseFragment() {
// First time login, need to select role (Student/Teacher)
    private lateinit var binding: FragmentRoleSelectionBinding
    override val viewModel: RoleSelectionViewModel by viewModels()
    private var currentRole = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRoleSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)

        binding.btnContinue.isVisible = false

        binding.btnStudent.setOnClickListener {
            changeRole("student")
        }


        binding.btnTeacher.setOnClickListener {
            changeRole("teacher")
        }

        binding.btnContinue.setOnClickListener {
            viewModel.setRole(currentRole)
            when(currentRole) {
                "teacher" -> findNavController().navigate(RoleSelectionFragmentDirections.actionRoleSelectionFragmentToTeacherDashboard(), NavOptions.Builder().setPopUpTo(findNavController().graph.startDestinationId, true).build())
                "student" -> findNavController().navigate(RoleSelectionFragmentDirections.actionRoleSelectionFragmentToStudentDashboard(), NavOptions.Builder().setPopUpTo(findNavController().graph.startDestinationId, true).build())
            }
        }
    }


    private fun changeRole(selectedRole: String) {
        currentRole = selectedRole
        binding.btnContinue.isVisible = true
        when(selectedRole) {
            "student" -> {
                binding.btnStudent.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.secondary_light))
                binding.btnTeacher.setBackgroundColor(ContextCompat.getColor(requireContext(), com.google.android.material.R.color.material_dynamic_neutral100))
            }
            "teacher" -> {
                binding.btnTeacher.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.secondary_light))
                binding.btnStudent.setBackgroundColor(ContextCompat.getColor(requireContext(), com.google.android.material.R.color.material_dynamic_neutral100))
            }
        }
    }

}