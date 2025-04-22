package com.quizapp.ui.student.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.quizapp.databinding.FragmentStudentDashboardBinding
import com.quizapp.ui.teacher.home.DashboardViewModel
import com.quizapp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

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
}