package com.quizapp.ui.teacher.add_quiz

import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.quizapp.data.model.Option
import com.quizapp.data.model.Question
import com.quizapp.databinding.FragmentAddQuizBinding
import com.quizapp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.UUID

@AndroidEntryPoint
class AddQuizFragment : BaseFragment() {
    private lateinit var binding: FragmentAddQuizBinding
    override val viewModel: AddQuizViewModel by viewModels()
    private fun processCSV(csvData: String) {
       val list = mutableListOf<Question>()
        val splitData = csvData.split("\n")
        for(l in splitData) {
            Log.d("listOfQuestions", l)
            // check if "l" is CSV header. if not then continue adding process
            if(l != splitData[0] && l.isNotEmpty()) {
                Log.d("listOfQuestions", "this")
                val split = l.split(",")
                // begin splitting = assuming the data follows the format specified in the CSV file
                // it should be 6 values
                if(split.size != 6) {
                    showError("Please strictly follow the format for the CSV file and check if every line follows the same format.")
                    return
                } else {
                    // index 0: question
                    // index 1-4: answers
                    // index 5: correct answer

                    list.add(Question(
                        id = UUID.randomUUID().toString(), // similar to manual add ID (random UUID)
                        text = split[0], // NOTE: if a question contains comma (also applies to answer) then it will detect as an invalid CSV file. this needs fixing
                        options = listOf(
                            // create an array with 4 questions
                            Option(text = split[1]),
                            Option(text = split[2]),
                            Option(text = split[3]),
                            Option(text = split[4])
                        ),
                        answer = split[5].toInt() // answer must be STRICTLY a number
                    ))
                }
            }
        }
        if(validateInput()) {
            viewModel.submitQuiz(binding.etTitle.text.toString(), binding.etDesc.text.toString(), list.toList())
        }

    }
    private val csvPickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
        // to get uri/content->(from the file user chose)
    ) { uri ->
        uri?.let {
            val fileName = getFileName(uri)
            if (fileName != null && !fileName.endsWith(".csv", ignoreCase = true)) {
                showError("This file is not a CSV file. Please select another file.")
                return@let
            }

            Log.d("debugging", "$it")

            try {
                requireContext().contentResolver.openInputStream(uri)?.use { inputStream ->
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val stringBuilder = StringBuilder()
                    var line: String?


                    while (reader.readLine().also { line = it } != null) {
                        stringBuilder.append(line).append("\n")
                    }

                    // Log the entire content
                    val fileContent = stringBuilder.toString()
                    processCSV(fileContent);
                }
            } catch (e: Exception) {
                showError("Invalid CSV file. Please select another CSV file, or fix the current CSV file and try again.")
            }
        }
    }
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

        binding.mbImportCsv.setOnClickListener {
            csvPickerLauncher.launch("*/*")
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

    private fun getFileName(uri: Uri): String? {
        // This queries the content provider associated with the uri and returns a Cursor.
        // A Cursor is like a pointer to a table of results.
        val cursor = requireContext().contentResolver.query(
            uri,
            null,
            null,
            null,
            null
        )
        cursor?.use {
            // This gets the index of the "DISPLAY_NAME" column,
            // which stores the file name (like "questions.csv").
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            // if return -1 prevent crash here
            if (nameIndex != -1 && it.moveToFirst()) {
                return it.getString(nameIndex)
            }
        }
        return null
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()
        lifecycleScope.launch {
            viewModel.success.collect {
                val quizId = viewModel.quizId.value
                quizId?.let {
                    findNavController().navigate(
                        AddQuizFragmentDirections
                            .addQuizSuccess(it)
                    )
                }
            }
        }
        lifecycleScope.launch {
            viewModel.error.collect { errorMsg ->
                showError(errorMsg)
            }
        }
    }
}