package com.quizapp.ui.teacher.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.quizapp.databinding.FragmentTeacherDashboardBinding
import com.quizapp.ui.base.BaseFragment
import com.quizapp.ui.teacher.adapters.QuizAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : BaseFragment() {
    private lateinit var binding: FragmentTeacherDashboardBinding
    override val viewModel: DashboardViewModel by viewModels()
    private lateinit var quizAdapter: QuizAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeacherDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)
    }
}