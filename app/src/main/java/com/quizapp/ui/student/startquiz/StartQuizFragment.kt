package com.quizapp.ui.student.startquiz

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
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
    private var quizId: String = ""
    private var quizSize: Int = -1
    private val args: StartQuizFragmentArgs by navArgs()
    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // shared preferences initialize
        preferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        editor = preferences.edit()

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

        binding.btnContinue.setOnClickListener {
            editor.putString("quizId", quizId)
            editor.putInt("totalQuestions", quizSize)
            editor.putInt("currentQuestion", 1)
            editor.putInt("currentPoints", 0)
            editor.putInt("correctAnswers", 0)
            editor.putInt("wrongAnswers", 0)
            editor.commit()

            val dir = StartQuizFragmentDirections.actionStartQuizFragmentToTakeQuizFragment(quizId, preferences.getInt("currentQuestion", -1))
            findNavController().navigate(dir)
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()
        lifecycleScope.launch {
            viewModel.quizInfo.collect {
                val base = viewModel.quizInfo.value
                binding.loading.isVisible = !base.quizLoaded
                binding.tvQuizName.text = base.quizName
                binding.tvQuizDesc.text = base.quizDescription
                quizId = base.quizId
                quizSize = base.quizSize
            }
        }
    }
}