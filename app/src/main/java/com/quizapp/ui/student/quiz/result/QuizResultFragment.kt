package com.quizapp.ui.student.quiz.result

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.quizapp.R
import com.quizapp.data.model.QuizHistory
import com.quizapp.databinding.FragmentQuizResultBinding
import com.quizapp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


// as this does not have a ViewModel, default fragment object is used
@AndroidEntryPoint
class QuizResultFragment : BaseFragment() {
    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var binding: FragmentQuizResultBinding
    override val viewModel: QuizResultViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // shared preferences initialize
        preferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        editor = preferences.edit()
        // Inflate the layout for this fragment
        binding = FragmentQuizResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)
        // initialize current points, correct and wrong answers for display purposes
        val currentPoints = preferences.getInt("currentPoints", -1)
        val correctAnswers = preferences.getInt("correctAnswers", -1)
        val wrongAnswers = preferences.getInt("wrongAnswers", -1)
        val quizId = preferences.getString("quizId", "")
        val quizName = preferences.getString("quizName", "")

        binding.run {
            tvPoints.text = getString(R.string.point_indicator, currentPoints.toString())
            tvCorrectAnswers.text = correctAnswers.toString()
            tvWrongAnswers.text = wrongAnswers.toString()
            // save current quiz into quiz history
            viewModel.saveQuiz(
                // quiz history object
                QuizHistory(
                    code = quizId!!,
                    correctQuestions = correctAnswers,
                    wrongQuestions = wrongAnswers,
                    points = currentPoints,
                    quizName = quizName
                )
            )
            btnContinue.setOnClickListener {
                // clear SharedPreferences to prepare for next quiz
                editor.clear()
                editor.commit()
                // navigate back to student dashboard
                val dir = QuizResultFragmentDirections.actionQuizResultFragmentToStudentDashboard()
                findNavController().navigate(
                    dir,
                    NavOptions.Builder()
                        .setPopUpTo(findNavController().graph.startDestinationId, true)
                        .build()
                )
            }
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()
        lifecycleScope.launch {
            viewModel.successfulSave.collect {
                binding.cvQuizSaved.isVisible = viewModel.successfulSave.value
            }
        }
    }

}