package com.quizapp.ui.teacher.add_quiz.manual

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.quizapp.R
import com.quizapp.databinding.FragmentManualAddQuizBinding
import com.quizapp.ui.base.BaseFragment
import com.quizapp.ui.teacher.adapters.QuestionAdapter

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
        setupRecyclerView()
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

    private fun setupRecyclerView() {
        questionAdapter = QuestionAdapter(emptyList())
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
//            saveQuiz()
        }
    }

    private fun setupObservers() {
        viewModel.questions.observe(viewLifecycleOwner) { questions ->
            questionAdapter.updateQuestions(questions)
            if (questions.isNotEmpty()) {
                binding.rvQuestions.smoothScrollToPosition(questions.size - 1)
            }
        }
    }

}