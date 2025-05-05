package com.quizapp.ui.teacher.add_quiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.quizapp.R
import com.quizapp.databinding.FragmentAddQuizBinding
import com.quizapp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddQuizFragment : BaseFragment() {
    private lateinit var binding: FragmentAddQuizBinding
    override val viewModel: AddQuizViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)

        setupToolbar()
        setupTextFields()
        setupButtons()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupTextFields() {
        binding.etTitle.addTextChangedListener {
            viewModel.setQuizTitle(it.toString())
        }

        binding.etDesc.addTextChangedListener {
            viewModel.setQuizDesc(it.toString())
        }
    }

    private fun setupButtons(){
        binding.mbAddManually.setOnClickListener {
            if(validateInput()){
                val action = AddQuizFragmentDirections.actionAddQuizFragmentToManualAddQuizFragment(
                    binding.etTitle.text.toString(),
                    binding.etDesc.text.toString()
                )
                findNavController().navigate(action)
            }
        }

        binding.mbImportCsv.setOnClickListener {
            Toast.makeText(context, "CSV import functionality coming soon!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateInput(): Boolean {
        if (binding.etTitle.text.toString().isBlank()) {
            binding.textInputLayoutQuizName.error = "Quiz name cannot be empty"
            return false
        } else {
            binding.textInputLayoutQuizName.error = null
        }
        return true
    }


}