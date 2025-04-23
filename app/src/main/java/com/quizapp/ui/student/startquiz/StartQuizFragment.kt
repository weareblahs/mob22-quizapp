package com.quizapp.ui.student.startquiz

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.quizapp.databinding.FragmentStartQuizBinding
import com.quizapp.ui.base.BaseFragment

class StartQuizFragment : BaseFragment() {
    private lateinit var binding: FragmentStartQuizBinding
    override val viewModel: StartQuizViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartQuizBinding.inflate(inflater, container, false)
        return binding.root
    }


}