package com.quizapp.ui.teacher.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.quizapp.data.model.Question
import com.quizapp.databinding.ItemShowQuestionBinding
import com.quizapp.ui.teacher.adapters.AddQuestionAdapter.QuestionViewHolder

class ShowQuestionAdapter(
    private var questionList: List<Question>
) : RecyclerView.Adapter<ShowQuestionAdapter.QuestionViewHolder>() {
    var listener: Listener ?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding = ItemShowQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questionList[position]
        holder.bind(question)
    }

    override fun getItemCount(): Int = questionList.size

    fun update(newQuestions: List<Question>) {
        this.questionList = newQuestions
        notifyDataSetChanged()
    }

    inner class QuestionViewHolder(
        private val binding: ItemShowQuestionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(question: Question) {
            binding.apply {
                textViewQuestionText.text = question.text
                textViewQuestionNumber.text = (adapterPosition + 1).toString()

                // Display correct answer
                if (question.options.isNotEmpty() && question.answer >= 0 && question.answer < question.options.size) {
                    textViewCorrectAnswer.text = question.options[question.answer].text
                }

                // Setup buttons
                btnEditQuestion.setOnClickListener {
                    listener?.onEditClick(question)
                }

                btnDeleteQuestion.setOnClickListener {
                    listener?.onDeleteClick(question)
                }
            }
        }
    }

    interface Listener {
        fun onEditClick(question: Question)
        fun onDeleteClick(question: Question)
    }
}