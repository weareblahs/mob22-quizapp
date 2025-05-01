package com.quizapp.ui.teacher.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.quizapp.data.model.Option
import com.quizapp.data.model.Question
import com.quizapp.databinding.ItemQuestionBinding

class QuestionAdapter(
    private var questionList: List<Question>
) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding = ItemQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    class QuestionViewHolder(private val binding: ItemQuestionBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(question: Question) {
            binding.tvQuestionText.text = question.text
            val options = question.options
            if (options.size >= 4) {
                binding.rbOption1.text = options[0].text
                binding.rbOption2.text = options[1].text
                binding.rbOption3.text = options[2].text
                binding.rbOption4.text = options[3].text
            }

            // Check the correct answer (assuming answer is stored as the index of the correct option)
            when (question.answer) {
                0 -> binding.rbOption1.isChecked = true
                1 -> binding.rbOption2.isChecked = true
                2 -> binding.rbOption3.isChecked = true
                3 -> binding.rbOption4.isChecked = true
            }
        }
    }
}
