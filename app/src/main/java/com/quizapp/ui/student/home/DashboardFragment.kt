package com.quizapp.ui.student.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.quizapp.databinding.FragmentStudentDashboardBinding
import com.quizapp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardFragment : BaseFragment() {
    override val viewModel: DashboardViewModel by viewModels()
    private lateinit var binding: FragmentStudentDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)
        // define code edit text endpoint
        binding.btnContinue.setOnClickListener {
              val code = binding.etCodeInput.text.toString()
            lifecycleScope.launch {
                viewModel.loadQuiz(code)
            }
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

//      QuizInfo
        lifecycleScope.launch{
            viewModel.quizInfo.collect {
                val base = viewModel.quizInfo.value
                Log.d("debugging", base.quizIsExist.toString())
                // loadQuiz performs actions here, in this case, this passes the quiz to the next view
                // which is a landing page for the student to select if they want to start the quiz or
                // not

            }
        }

    }
}