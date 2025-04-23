package com.quizapp.ui.teacher.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.quizapp.data.model.Question
import com.quizapp.data.model.QuestionType
import com.quizapp.databinding.ItemQuestionBinding

class QuestionAdapter(
    private var questions: List<Question> = emptyList(),
) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemQuestionBinding.inflate(inflater, parent, false)
        return QuestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.bind(questions[position])
    }

    override fun getItemCount(): Int = questions.size

    fun updateQuestions(newQuestions: List<Question>) {
        this.questions = newQuestions
        notifyDataSetChanged()
    }

    inner class QuestionViewHolder(private val binding: ItemQuestionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(question: Question) = with(binding) {
            tvTitle.text = "Question ${adapterPosition + 1}"

            // Setup question type switch
            msQuestionType.apply {
                isChecked = question.type == QuestionType.MULTIPLE_CHOICE
                text = if (isChecked) "Multiple Choice" else "Single Choice"

                setOnCheckedChangeListener { _, isChecked ->
                    val isNowSingle = !isChecked
                    text = if (isChecked) "Multiple Choice" else "Single Choice"
                    listener?.onQuestionTypeChanged(position, isChecked)

                    if (isNowSingle) {
                        // Keep only the first checked checkbox
                        val checkBoxes = getCheckBoxes()
                        var firstFound = false
                        checkBoxes.forEach { checkBox ->
                            if (checkBox.isChecked) {
                                if (!firstFound) {
                                    firstFound = true
                                } else {
                                    checkBox.isChecked = false
                                }
                            }
                        }
                    }

                    setupCheckBoxListeners(position, isNowSingle)
                }
            }

            // Set question text change listener
            etQuestion.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    listener?.onQuestionTextChanged(position, etQuestion.text.toString())
                }
            }

            // Populate option texts and checkboxes
            if (question.options.isNotEmpty()) {
                etOptionA.setText(question.options.getOrNull(0)?.text ?: "")
                checkBoxOptionA.isChecked = question.options.getOrNull(0)?.isCorrect ?: false

                etOptionB.setText(question.options.getOrNull(1)?.text ?: "")
                checkBoxOptionB.isChecked = question.options.getOrNull(1)?.isCorrect ?: false

                etOptionC.setText(question.options.getOrNull(2)?.text ?: "")
                checkBoxOptionC.isChecked = question.options.getOrNull(2)?.isCorrect ?: false

                etOptionD.setText(question.options.getOrNull(3)?.text ?: "")
                checkBoxOptionD.isChecked = question.options.getOrNull(3)?.isCorrect ?: false
            }

            // Set option text listeners
            etOptionA.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) listener?.onOptionTextChanged(position, 0, etOptionA.text.toString())
            }
            etOptionB.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) listener?.onOptionTextChanged(position, 1, etOptionB.text.toString())
            }
            etOptionC.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) listener?.onOptionTextChanged(position, 2, etOptionC.text.toString())
            }
            etOptionD.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) listener?.onOptionTextChanged(position, 3, etOptionD.text.toString())
            }

            setupCheckBoxListeners(position, question.type != QuestionType.MULTIPLE_CHOICE)
        }

        private fun setupCheckBoxListeners(position: Int, isSingleChoice: Boolean) {
            val checkBoxes = getCheckBoxes()

            checkBoxes.forEachIndexed { index, checkBox ->
                checkBox.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        listener?.onCorrectOptionSelected(position, index)

                        if (isSingleChoice) {
                            checkBoxes.forEachIndexed { otherIndex, otherCheckBox ->
                                if (otherIndex != index && otherCheckBox.isChecked) {
                                    otherCheckBox.isChecked = false
                                }
                            }
                        }
                    }
                }
            }
        }

        private fun getCheckBoxes(): List<CheckBox> = listOf(
            binding.checkBoxOptionA,
            binding.checkBoxOptionB,
            binding.checkBoxOptionC,
            binding.checkBoxOptionD
        )
    }

    interface Listener {
        fun onQuestionTextChanged(position: Int, text: String)
        fun onOptionTextChanged(questionPosition: Int, optionPosition: Int, text: String)
        fun onCorrectOptionSelected(questionPosition: Int, optionPosition: Int)
        fun onQuestionTypeChanged(position: Int, isMultipleChoice: Boolean)
    }
}
