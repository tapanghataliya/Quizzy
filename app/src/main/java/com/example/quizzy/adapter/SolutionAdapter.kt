package com.example.quizzy.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.quizzy.R
import com.example.quizzy.data.quiz.Results
import com.example.quizzy.databinding.ItemsQuizBinding
import com.example.quizzy.ui.quiz.QuizeViewModel
import com.example.quizzy.ui.settings.SettingsViewModel
import com.example.quizzy.ui.solution.SolutionViewModel

class SolutionAdapter(
    private val solutionViewModel: SolutionViewModel,
    private val context: Context,
    private val settingsViewModel: SettingsViewModel
) : RecyclerView.Adapter<SolutionAdapter.ViewPagerHolder>() {

    private var addAnsList = mutableListOf<String>()

    class ViewPagerHolder(val view: ItemsQuizBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerHolder {
        val binding = ItemsQuizBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewPagerHolder(binding)
    }

    private val differCallback = object : DiffUtil.ItemCallback<Results>() {
        override fun areItemsTheSame(
            oldItem: Results,
            newItem: Results
        ): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: Results,
            newItem: Results
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)
    fun questionList(list: List<Results>) = differ.submitList(list)

    override fun onBindViewHolder(holder: ViewPagerHolder, position: Int) {
        val quize = differ.currentList[position]
        holder.view.txtQuestion.text = quize.question
        holder.view.txtCategory.text = quize.category

        val queNumber = position + 1
        holder.view.txtAttempt.text =
            queNumber.toString() + " / " + differ.currentList.size.toString()

        addAnsList.clear()
        addAnsList.add(quize.correct_answer!!)
        for (i in quize.incorrect_answers!!) {
            addAnsList.add(i)
        }
        addAnsList.shuffle()

        for (i in 0 until addAnsList.size){
            // Add new views dynamically here
            // Programmatically inflate the sub-layout
            val subLayout = LayoutInflater.from(holder.itemView.context)
                .inflate(R.layout.custom_items_answer, holder.view.lylCustomTextview, false)
            val textAnswer = subLayout.findViewById<TextView>(R.id.txtAns)
            textAnswer.text = addAnsList[i]
            val quizAnswer = textAnswer.text.toString()

            if (quize.correct_answer == quizAnswer){
                textAnswer.setBackgroundResource(R.color.green)
                textAnswer.setTextColor(Color.WHITE)
            }else{
                textAnswer.setBackgroundResource(R.color.white)
            }
            holder.view.lylCustomTextview.addView(subLayout)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}