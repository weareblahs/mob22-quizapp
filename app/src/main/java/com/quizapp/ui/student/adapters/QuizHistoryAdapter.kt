package com.quizapp.ui.student.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.quizapp.data.model.QuizHistory
import com.quizapp.databinding.ItemRedoQuizBinding

class QuizHistoryAdapter(
    private var quizHistory: List<QuizHistory>
) : RecyclerView.Adapter<QuizHistoryAdapter.QuizHistoryViewHolder>() {
    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizHistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRedoQuizBinding.inflate(inflater, parent, false)
        return QuizHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuizHistoryViewHolder, position: Int) {
        holder.bind(quizHistory[position])
    }


    override fun getItemCount(): Int {
        return quizHistory.size
    }

    inner class QuizHistoryViewHolder(private val binding: ItemRedoQuizBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(quiz: QuizHistory) {
            binding.run {
                tvCode.text = quiz.code
                tvQuizName.text = quiz.quizName
                tvPointsIndicator.text = quiz.points.toString()
                tvCorrectIndicator.text = quiz.correctQuestions.toString()
                tvWrongIndicator.text = quiz.wrongQuestions.toString()
            }
            binding.cvQuizItem.setOnClickListener {
                listener?.onItemClick(quiz)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setQuizHistory(newQuizHistory: List<QuizHistory>) {
        this.quizHistory = newQuizHistory
        notifyDataSetChanged()
    }

    interface Listener{
        fun onItemClick(quiz: QuizHistory)
    }
}