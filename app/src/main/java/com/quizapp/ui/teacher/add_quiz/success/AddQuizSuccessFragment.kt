package com.quizapp.ui.teacher.add_quiz.success

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.quizapp.R
import com.quizapp.databinding.FragmentAddQuizSuccessBinding
import com.quizapp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddQuizSuccessFragment : BaseFragment() {
    private lateinit var binding: FragmentAddQuizSuccessBinding
    override val viewModel: AddQuizSuccessViewModel by viewModels()
    private val args: AddQuizSuccessFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddQuizSuccessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)
        Log.d("debugging", "setupUiComponents: ${args.quizId}")
        viewModel.setQuizId(args.quizId)

        setupButtons()
        updateQuizCodeDisplay()
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        lifecycleScope.launch {
            viewModel.quizId.collect { quizId ->
                binding.textViewQuizCode.text = quizId
            }
        }
    }

    private fun updateQuizCodeDisplay() {
        binding.textViewSuccessMessage.text = "Your quiz has been successfully created and saved.\nQuiz Code:"
    }

    private fun setupButtons() {
        binding.buttonShare.setOnClickListener {
            shareQuizCode()
        }

        binding.buttonCopyCode.setOnClickListener {
            copyQuizCodeToClipboard()
        }

        binding.buttonBackToHome.setOnClickListener {
            navigateToHome()
        }
    }

    private fun shareQuizCode() {
        val quizId = viewModel.quizId.value
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Join my quiz using this code: $quizId")
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Share Quiz Code"))
    }

    private fun copyQuizCodeToClipboard() {
        val quizId = viewModel.quizId.value
        val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Quiz Code", quizId)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(requireContext(), "Quiz code copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    private fun navigateToHome() {
        // Navigate to the home fragment
        val action =
            AddQuizSuccessFragmentDirections.actionAddQuizSuccessFragmentToTeacherDashboard()
        findNavController().navigate(action)
    }
}