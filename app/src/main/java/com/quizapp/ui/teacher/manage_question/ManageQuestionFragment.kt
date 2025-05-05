package com.quizapp.ui.teacher.manage_question

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputEditText
import com.quizapp.databinding.FragmentManageQuestionBinding
import com.quizapp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ManageQuestionFragment : BaseFragment() {
    private lateinit var binding: FragmentManageQuestionBinding
    override val viewModel: ManageQuestionViewModel by viewModels()
    private val args: ManageQuestionFragmentArgs by navArgs()

    private lateinit var optionEditTexts: List<TextInputEditText>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentManageQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)
        viewModel.initialize(args.quizId, args.questionId)

        optionEditTexts = listOf(
            binding.etOption1,
            binding.etOption2,
            binding.etOption3,
            binding.etOption4
        )

        setupToolbar()

        binding.rbOption1.isChecked = true

        binding.btnSaveQuestion.setOnClickListener {
            saveQuestion()
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        lifecycleScope.launch {
            viewModel.isEditMode.collect { isEditMode ->
                binding.toolbar.title = if (isEditMode) "Edit Question" else "Add Question"
                binding.btnSaveQuestion.text = if (isEditMode) "Update Question" else "Add Question"
            }
        }

        lifecycleScope.launch {
            viewModel.question.collect { question ->
                question?.let {
                    binding.editTextQuestion.setText(it.text)

                    for (i in it.options.indices.take(4)) {
                        optionEditTexts[i].setText(it.options[i].text)
                    }

                    when (it.answer) {
                        0 -> binding.rbOption1.isChecked = true
                        1 -> binding.rbOption2.isChecked = true
                        2 -> binding.rbOption3.isChecked = true
                        3 -> binding.rbOption4.isChecked = true
                        else -> binding.rbOption1.isChecked = true
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.loading.collect { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }

        // ✅ Handle success event
        lifecycleScope.launch {
            viewModel.questionSavedSuccess.collect { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            } 
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun getSelectedOptionIndex(): Int {
        return when {
            binding.rbOption1.isChecked -> 0
            binding.rbOption2.isChecked -> 1
            binding.rbOption3.isChecked -> 2
            binding.rbOption4.isChecked -> 3
            else -> 0
        }
    }

    private fun saveQuestion() {
        val questionText = binding.editTextQuestion.text.toString().trim()
        val options = optionEditTexts.map { it.text.toString().trim() }
        val correctAnswerIndex = getSelectedOptionIndex()

        if (questionText.isEmpty()) {
            showError("Please enter a question")
            return
        }

        val filledOptionsCount = options.count { it.isNotEmpty() }

        if (filledOptionsCount < 2) {
            showError("Please enter at least two options")
            return
        }

        if (options[correctAnswerIndex].isEmpty()) {
            showError("The selected correct answer cannot be empty")
            return
        }

        viewModel.saveQuestion(questionText, options, correctAnswerIndex)
    }
}
