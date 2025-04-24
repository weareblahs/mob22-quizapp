package com.quizapp.ui.teacher.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.quizapp.R
import com.quizapp.databinding.FragmentQuizDetailBinding
import com.quizapp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class QuizDetailFragment : BaseFragment() {
    private lateinit var binding: FragmentQuizDetailBinding
    override val viewModel: QuizDetailViewModel by viewModels()
    private val args: QuizDetailFragmentArgs by navArgs()

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
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()
//        viewModel.getQuizDetail()
//        viewModel.quizDetail.observe(viewLifecycleOwner) { quiz ->
//            binding.textViewTitle.text = quiz.title
//            binding.textViewDesc.text = quiz.description
//            binding.textViewCode.text = quiz.code
//        }
    }
}