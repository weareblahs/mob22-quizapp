package com.quizapp.ui.student.quiz.result

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.quizapp.R
import com.quizapp.databinding.FragmentQuizResultBinding
import dagger.hilt.android.AndroidEntryPoint


// as this does not have a ViewModel, default fragment object is used
@AndroidEntryPoint
class QuizResultFragment : Fragment() {

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var binding: FragmentQuizResultBinding
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // initialize current points, correct and wrong answers for display purposes
        val currentPoints = preferences.getInt("currentPoints", -1)
        val correctAnswers = preferences.getInt("correctAnswers", -1)
        val wrongAnswers = preferences.getInt("wrongAnswers", -1)

        binding.run {
            tvPoints.text = getString(R.string.point_indicator, currentPoints.toString())
            tvCorrectAnswers.text = correctAnswers.toString()
            tvWrongAnswers.text = wrongAnswers.toString()
            btnContinue.setOnClickListener {
//              // navigate back to student dashboard
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

}