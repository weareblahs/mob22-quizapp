package com.quizapp.ui.teacher.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.quizapp.data.model.Quiz
import com.quizapp.databinding.FragmentTeacherDashboardBinding
import com.quizapp.ui.base.BaseFragment
import com.quizapp.ui.teacher.adapters.QuizAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
        setupAdapter()
        binding.fabAddQuiz.setOnClickListener{
//            viewModel.addDummyQuiz()
            val action = DashboardFragmentDirections.actionTeacherDashboardToAddQuizFragment()
            findNavController().navigate(action)
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()
        lifecycleScope.launch {
            viewModel.quiz.collect { quiz ->
                Log.d("debugging", "setupViewModelObserver: $quiz")
                quizAdapter.setQuizs(quiz)
            }
        }
    }

    private fun setupAdapter() {
        quizAdapter = QuizAdapter(emptyList())
        binding.rvQuizzes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvQuizzes.adapter = quizAdapter
        quizAdapter.listener = object : QuizAdapter.Listener {
            override fun onItemClick(quiz: Quiz) {
                // Handle item click
                val action = DashboardFragmentDirections.actionTeacherDashboardToQuizDetailFragment(quiz.id!!)
                findNavController().navigate(action)
            }

            override fun onDeleteClick(quiz: Quiz) {
                // Handle delete click
                fun deleteQuiz() {
                    viewModel.deleteQuiz(quiz)
                }
                showDialog(
                    "Delete Quiz",
                    "Are you sure you want to delete this quiz?",
                    "Delete",
                    ::deleteQuiz,
                    true,
                    "Quiz Delete Successfully"
                )
            }
        }
    }


}