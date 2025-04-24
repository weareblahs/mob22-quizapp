package com.quizapp.ui.teacher.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.quizapp.data.model.Question
import com.quizapp.databinding.ItemQuestionBinding

class QuestionAdapter(
    private var questions: List<Question> = emptyList(),
) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    var listener: Listener? = null

    // Set stable IDs to improve performance
    init {
        setHasStableIds(true)
    }

    // Override getItemId to return a stable ID for each item
    override fun getItemId(position: Int): Long {
        return questions[position].id?.toLongOrNull() ?: position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemQuestionBinding.inflate(inflater, parent, false)
        return QuestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.bind(questions[position])
    }

    override fun getItemCount(): Int = questions.size

    override fun onViewRecycled(holder: QuestionViewHolder) {
        super.onViewRecycled(holder)
        holder.itemView.clearFocus()
    }

    fun updateQuestions(newQuestions: List<Question>) {
        this.questions = newQuestions
        notifyDataSetChanged()
    }

    inner class QuestionViewHolder(private val binding: ItemQuestionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(question: Question) = with(binding) {
            tvTitle.text = "Question ${adapterPosition + 1}"

            // Update question text only if it’s different
            if (etQuestion.text.toString() != question.text) {
                etQuestion.setText(question.text)
            }

            etQuestion.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    listener?.onQuestionTextChanged(adapterPosition, etQuestion.text.toString())
                }
            }

            // Set options text and checkbox states
            question.options.getOrNull(0)?.let {
                if (etOptionA.text.toString() != it.text) etOptionA.setText(it.text)
                checkBoxOptionA.isChecked = it.isCorrect
            }

            question.options.getOrNull(1)?.let {
                if (etOptionB.text.toString() != it.text) etOptionB.setText(it.text)
                checkBoxOptionB.isChecked = it.isCorrect
            }

            question.options.getOrNull(2)?.let {
                if (etOptionC.text.toString() != it.text) etOptionC.setText(it.text)
                checkBoxOptionC.isChecked = it.isCorrect
            }

            question.options.getOrNull(3)?.let {
                if (etOptionD.text.toString() != it.text) etOptionD.setText(it.text)
                checkBoxOptionD.isChecked = it.isCorrect
            }

            // Option text change listeners
            etOptionA.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) listener?.onOptionTextChanged(adapterPosition, 0, etOptionA.text.toString())
            }
            etOptionB.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) listener?.onOptionTextChanged(adapterPosition, 1, etOptionB.text.toString())
            }
            etOptionC.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) listener?.onOptionTextChanged(adapterPosition, 2, etOptionC.text.toString())
            }
            etOptionD.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) listener?.onOptionTextChanged(adapterPosition, 3, etOptionD.text.toString())
            }

            // Prevent checkbox from stealing focus
            listOf(checkBoxOptionA, checkBoxOptionB, checkBoxOptionC, checkBoxOptionD).forEach {
                it.isFocusable = false
                it.isFocusableInTouchMode = false
            }

            // Set up checkbox logic
            setupCheckBoxListeners(adapterPosition)
        }

        private fun setupCheckBoxListeners(position: Int) {
            val checkBoxes = getCheckBoxes()

            // Remove old listeners
            checkBoxes.forEach { it.setOnCheckedChangeListener(null) }

            checkBoxes.forEachIndexed { index, checkBox ->
                checkBox.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        // Single-choice logic: uncheck others
                        checkBoxes.forEachIndexed { i, cb ->
                            if (i != index) cb.isChecked = false
                        }
                    } else {
                        // Prevent all checkboxes from being unchecked
                        if (checkBoxes.none { it.isChecked }) {
                            checkBox.isChecked = true
                            return@setOnCheckedChangeListener
                        }
                    }

                    listener?.onCorrectOptionChanged(position, index, isChecked)
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
        fun onCorrectOptionChanged(questionPosition: Int, optionPosition: Int, isChecked: Boolean)
    }
}
