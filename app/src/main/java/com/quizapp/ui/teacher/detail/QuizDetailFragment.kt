package com.quizapp.ui.teacher.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.quizapp.R
import com.quizapp.data.model.Question
import com.quizapp.databinding.FragmentQuizDetailBinding
import com.quizapp.ui.base.BaseFragment
import com.quizapp.ui.teacher.adapters.ShowQuestionAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class QuizDetailFragment : BaseFragment() {
    private lateinit var binding: FragmentQuizDetailBinding
    override val viewModel: QuizDetailViewModel by viewModels()
    private val args: QuizDetailFragmentArgs by navArgs()
    private lateinit var questionAdapter: ShowQuestionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentQuizDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)
        viewModel.setQuizId(args.quizId)
//        setupToolbar()

//        binding.btnDeleteQuiz.setOnClickListener {
//            showDeleteQuizConfirmation()
//        }
        setupAdapter()
        binding.fabAddQuestion.setOnClickListener {
            val action = QuizDetailFragmentDirections.actionQuizDetailFragmentToManageQuestionFragment(
                questionId = "",
                quizId = args.quizId
            )
            findNavController().navigate(action)
        }

    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()
        lifecycleScope.launch {
            viewModel.quizDetail.collect{
                Log.d("debugging", "setupViewModelObserver: $it")
                binding.textViewTitle.text = it?.title
                binding.textViewDescription.text = if(it?.description != ""){
                    it?.description
                }else{
                    "No description"
                }
            }
        }

        lifecycleScope.launch {
            viewModel.questions.collect { questions ->
                questionAdapter.update(questions)
                binding.emptyStateLayout.isVisible = questions.isEmpty()
            }
        }

    }

    private fun setupAdapter() {
        questionAdapter = ShowQuestionAdapter(emptyList())
        binding.rvQuestions.adapter = questionAdapter
        binding.rvQuestions.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(requireContext())
        questionAdapter.listener = object : ShowQuestionAdapter.Listener {
            override fun onEditClick(question: Question) {
                Log.d("debugging", "onEditClick: $question")
                val action = QuizDetailFragmentDirections.actionQuizDetailFragmentToManageQuestionFragment(
                    quizId = args.quizId,
                    questionId = question.id ?: ""
                )
                findNavController().navigate(action)
            }

            override fun onDeleteClick(question: Question) {
                showDeleteQuestionConfirmation(question)
            }
        }
    }

    private fun showDeleteQuestionConfirmation(question: Question) {
        showDialog(
            "Delete Question",
            "Are you sure you want to delete this question?",
            "Delete",
            { deleteQuestion(question.id ?: "") },
            true,
            "Question deleted successfully"
        )
    }

    private fun showDeleteQuizConfirmation() {
        showDialog(
            "Delete Quiz",
            "Are you sure you want to delete this quiz?",
            "Delete",
            ::deleteQuiz,
            true,
            "Quiz deleted successfully"
        )
    }


    private fun deleteQuestion(questionId: String) {
        viewModel.deleteQuestion(questionId)
    }

    private fun deleteQuiz() {
        // Navigate back after deleting the quiz
        findNavController().navigateUp()
    }

}