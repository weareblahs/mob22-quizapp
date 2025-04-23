package com.quizapp.ui.student.startquiz

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.quizapp.databinding.FragmentStartQuizBinding
import com.quizapp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StartQuizFragment : BaseFragment() {
    override val viewModel: StartQuizViewModel by viewModels()
    private lateinit var binding: FragmentStartQuizBinding
    private val args: StartQuizFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)
        lifecycleScope.launch {
            viewModel.loadQuiz(args.code)
        }

        binding.btnCancel.setOnClickListener {
            // Set fragment result to reset the quiz info
            setFragmentResult("reset_quiz_request", Bundle().apply {
                putBoolean("should_reset", true)
            })

            // Navigate back
            findNavController().popBackStack()
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()
        lifecycleScope.launch {
            viewModel.quizInfo.collect {
                val base = viewModel.quizInfo.value
                binding.tvQuizName.text = base.quizName
                binding.tvQuizDesc.text = base.quizDescription
            }
        }
    }
}