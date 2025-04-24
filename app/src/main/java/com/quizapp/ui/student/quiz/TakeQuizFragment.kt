package com.quizapp.ui.student.quiz

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.quizapp.R
import com.quizapp.data.model.Question
import com.quizapp.databinding.FragmentTakeQuizBinding
import com.quizapp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TakeQuizFragment : BaseFragment() {
    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var binding: FragmentTakeQuizBinding
    override val viewModel: TakeQuizViewModel by viewModels()
    private var answer = -1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // shared preferences initialize
        preferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        editor = preferences.edit()
        // Inflate the layout for this fragment
        binding = FragmentTakeQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)
        // initialize sharedPreferences data
        val quizId = preferences.getString("quizId", "")

        val currentQuestion = preferences.getInt("currentQuestion", -1)
        val currentPoints = preferences.getInt("currentPoints", -1)


//        set points to current points
        binding.tvPoints.text = getString(R.string.point_indicator, currentPoints.toString())

        viewModel.retrieveQuestion(quizId!!, currentQuestion)

        binding.run {
            cvAnswer1.setOnClickListener {
                selectAnswer(1)
            }
            cvAnswer2.setOnClickListener {
                selectAnswer(2)
            }
            cvAnswer3.setOnClickListener {
                selectAnswer(3)
            }
            cvAnswer4.setOnClickListener {
                selectAnswer(4)
            }
            btnContinue.setOnClickListener {
                submitAnswer(answer)
            }
        }
    }

    private fun submitAnswer(answer: Int) {
        val ca = viewModel.question.value.options.filter {
            it.isCorrect
        }[0].text // correct answer for this question (if single selection)
        val currentPoints = preferences.getInt("currentPoints", -1)
        val currentQuestion = preferences.getInt("currentQuestion", -1)
        val totalQuestions = preferences.getInt("totalQuestions", -1)
        val correctAnswers = preferences.getInt("correctAnswers", -1)
        val wrongAnswers = preferences.getInt("wrongAnswers", -1)
        val quizId = preferences.getString("quizId", "")
        val ic = viewModel.question.value.options[answer-1].isCorrect
        binding.correctAnswer.isVisible = ic
        binding.wrongAnswer.isVisible = !ic

        if(ic) {
            editor.putInt("currentPoints", currentPoints + 10)
            editor.putInt("correctAnswers", correctAnswers + 1)
        } else {
            editor.putInt("wrongAnswers", wrongAnswers + 1)
            binding.correctAnswerText.text =
                getString(R.string.student_correct_answer_description_display, ca)
        }
        // GLOBAL: correct / wrong
        // disable all answer bindings first
            binding.run {
                val answerViews = arrayOf(cvAnswer1, cvAnswer2, cvAnswer3, cvAnswer4)
                answerViews.forEach { it.setOnClickListener {} }
            }
        // add current question
            editor.putInt("currentQuestion", currentQuestion + 1)
            editor.commit()
        // add continue button action
            binding.btnContinue.setOnClickListener {
            // when pressed continue button again, go to either result page or next question
            if(currentQuestion == totalQuestions) {
                // the following runs if this is the last question which is compared with the condition above
                val dir = TakeQuizFragmentDirections.actionTakeQuizFragmentToQuizResultFragment()
                findNavController().navigate(dir)
            } else {
                // else navigate to next question (self direction)
                val dir = TakeQuizFragmentDirections.nextQuestion(quizId!!, currentQuestion)
                findNavController().navigate(dir)
            }
        }
    }

    private fun selectAnswer(answerIndex: Int) {
        answer = answerIndex
        binding.run {
            val highlightColor = ContextCompat.getColor(requireContext(), R.color.secondary_light)
            val defaultColor = ContextCompat.getColor(requireContext(), com.google.android.material.R.color.material_dynamic_neutral100)

            val answerViews = arrayOf(cvAnswer1, cvAnswer2, cvAnswer3, cvAnswer4)

            answerViews.forEach { it.setBackgroundColor(defaultColor) }

            if (answerIndex in 1..4) {
                answerViews[answerIndex - 1].setBackgroundColor(highlightColor)
            }
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()
        lifecycleScope.launch {
            viewModel.question.collect {
                val base = viewModel.question.value
                if(base != Question()) {
                    binding.loading.isVisible = false
                    binding.tvQuestion.text = base.text
                    binding.answer1Text.text = base.options[0].text
                    binding.answer2Text.text = base.options[1].text
                    binding.answer3Text.text = base.options[2].text
                    binding.answer4Text.text = base.options[3].text
                }
            }
        }
    }


}