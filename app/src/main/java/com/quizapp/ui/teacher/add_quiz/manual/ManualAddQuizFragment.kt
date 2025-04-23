package com.quizapp.ui.teacher.add_quiz.manual

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.quizapp.R
import com.quizapp.databinding.FragmentManualAddQuizBinding
import com.quizapp.ui.base.BaseFragment
import com.quizapp.ui.teacher.adapters.QuestionAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ManualAddQuizFragment : BaseFragment() {
    private lateinit var binding: FragmentManualAddQuizBinding
    private val args: ManualAddQuizFragmentArgs by navArgs()
    override val viewModel: ManualAddQuizViewModel by viewModels()
    private lateinit var questionAdapter: QuestionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentManualAddQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)
        viewModel.setQuizInfo(args.title, args.desc)

        setupToolbar()
        setupAdapter()
        setupButtons()
//        setupObservers()
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()
        setupObservers()
    }


    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupAdapter() {
        questionAdapter = QuestionAdapter(emptyList())
        questionAdapter.listener = object : QuestionAdapter.Listener {
            override fun onQuestionTextChanged(position: Int, text: String) {
                viewModel.updateQuestionText(position, text)
            }

            override fun onOptionTextChanged(questionPosition: Int, optionPosition: Int, text: String) {
                viewModel.updateOptionText(questionPosition, optionPosition, text)
            }

            override fun onCorrectOptionSelected(questionPosition: Int, optionPosition: Int) {
                viewModel.setCorrectOption(questionPosition, optionPosition)
            }

            override fun onQuestionTypeChanged(position: Int, isMultipleChoice: Boolean) {
                viewModel.updateQuestionType(position, isMultipleChoice)
            }

        }
        binding.rvQuestions.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = questionAdapter
        }
    }


    private fun setupButtons() {
        binding.fabAddQuestion.setOnClickListener {
            viewModel.addNewQuestion()
        }

        binding.mbSave.setOnClickListener {
            viewModel.createQuiz()
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.questions.collect { questions ->
                Log.d("debugging", "setupObservers: $questions")
                questionAdapter.updateQuestions(questions)
            }
        }
        lifecycleScope.launch {
            viewModel.success.collect{
                Log.d("debugging", "setupObservers: $it")
                val action = ManualAddQuizFragmentDirections.actionManualAddQuizFragmentToAddQuizSuccessFragment()
                findNavController().navigate(action)

            }
        }
    }

}