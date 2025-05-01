package com.quizapp.ui.teacher.add_quiz.manual

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.quizapp.R
import com.quizapp.data.model.Option
import com.quizapp.data.model.Question
import com.quizapp.data.model.Quiz
import com.quizapp.databinding.FragmentManualAddQuizBinding
import com.quizapp.ui.base.BaseFragment
import com.quizapp.ui.quiz.ManualAddQuizViewModel
import com.quizapp.ui.teacher.adapters.QuestionAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ManualAddQuizFragment : BaseFragment() {
    private lateinit var binding: FragmentManualAddQuizBinding
    private val args: ManualAddQuizFragmentArgs by navArgs()
    override val viewModel: ManualAddQuizViewModel by viewModels()

    private lateinit var questionAdapter: QuestionAdapter
    private var quizTitle = ""
    private var quizDescription = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentManualAddQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUiComponents(view: View) {
        quizTitle = args.title
        quizDescription = args.desc

        setupRecyclerView()
        setupClickListeners()
    }

    override fun setupViewModelObserver() {
        lifecycleScope.launch {
            viewModel.questionList.collect { questions ->
                questionAdapter.update(questions)
            }
        }

        lifecycleScope.launch {
            viewModel.success.collect {
                val quizId = viewModel.quizId.value
                quizId?.let {
                    findNavController().navigate(
                        ManualAddQuizFragmentDirections
                            .actionManualAddQuizFragmentToAddQuizSuccessFragment(it)
                    )
                }
            }
        }


        lifecycleScope.launch {
            viewModel.error.collect { errorMsg ->
                errorMsg?.let { showError(it) }
            }
        }
    }

    private fun setupRecyclerView() {
        questionAdapter = QuestionAdapter(emptyList())
        binding.rvQuestions.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = questionAdapter
        }
    }

    private fun setupClickListeners() {
        binding.btnSubmitQuestion.setOnClickListener {
            val question = getQuestionFromInput()
            if (question != null) {
                viewModel.addQuestion(question)
                clearInputs() // clear right after adding
                binding.btnSubmitQuestion.visibility = View.GONE
                binding.layoutAfterSubmit.visibility = View.VISIBLE
            } else {
                showError("Please fill in all fields and select a correct answer")
            }
        }

        binding.btnAddNextQuestion.setOnClickListener {
            val question = getQuestionFromInput()
            if (question != null) {
                viewModel.addQuestion(question)
                clearInputs() // clear right after adding
            } else {
                showError("Please fill in all fields and select a correct answer")
            }
        }

        binding.btnSubmitQuiz.setOnClickListener {
            viewModel.submitQuiz(quizTitle, quizDescription)
        }
    }

    private fun getQuestionFromInput(): Question? {
        val questionText = binding.etQuestionText.text.toString().trim()
        val option1 = binding.etOption1.text.toString().trim()
        val option2 = binding.etOption2.text.toString().trim()
        val option3 = binding.etOption3.text.toString().trim()
        val option4 = binding.etOption4.text.toString().trim()

        val correctIndex = when (binding.rgCorrectOption.checkedRadioButtonId) {
            R.id.rbOption1 -> 0
            R.id.rbOption2 -> 1
            R.id.rbOption3 -> 2
            R.id.rbOption4 -> 3
            else -> -1
        }

        if (questionText.isBlank() || option1.isBlank() || option2.isBlank() ||
            option3.isBlank() || option4.isBlank() || correctIndex == -1
        ) return null

        return Question(
            text = questionText,
            options = listOf(
                Option(option1),
                Option(option2),
                Option(option3),
                Option(option4)
            ),
            answer = correctIndex
        )
    }

    private fun clearInputs() {
        binding.etQuestionText.setText("")
        binding.etOption1.setText("")
        binding.etOption2.setText("")
        binding.etOption3.setText("")
        binding.etOption4.setText("")
        binding.rgCorrectOption.clearCheck()
    }
}

