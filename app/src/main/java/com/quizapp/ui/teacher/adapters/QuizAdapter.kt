package com.quizapp.ui.teacher.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.quizapp.data.model.Quiz
import com.quizapp.databinding.ItemQuizBinding

class QuizAdapter(
    private var quizs: List<Quiz>
) : RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {
    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemQuizBinding.inflate(inflater, parent, false)
        return QuizViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        holder.bind(quizs[position])
    }


    override fun getItemCount(): Int {
        return quizs.size
    }

    inner class QuizViewHolder(private val binding: ItemQuizBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(quiz: Quiz) {
            binding.tvTitle.text = quiz.title
            binding.tvDesc.text = quiz.description
            binding.tvQuizCount.text = quiz.questions.size.toString()
            binding.cvQuiz.setOnClickListener { listener?.onItemClick(quiz) }
            binding.ivDelete.setOnClickListener {
                listener?.onDeleteClick(quiz)
            }
        }
    }

    fun setQuizs(newQuizs: List<Quiz>) {
        this.quizs = newQuizs
        notifyDataSetChanged()
    }

    interface Listener{
        fun onItemClick(quiz: Quiz)
        fun onDeleteClick(quiz: Quiz)
    }
}