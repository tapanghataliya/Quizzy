package com.example.quizzy.features.question.presentation.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.quizzy.R
import com.example.quizzy.databinding.ItemsQuizBinding
import com.example.quizzy.features.question.data.model.Results
import java.util.*


@Suppress("UNREACHABLE_CODE", "DEPRECATION")
class QuestionAdapter : RecyclerView.Adapter<QuestionAdapter.ViewPagerHolder>(){

    private lateinit var subLayout: View
    private val answerSet = HashSet<String>()
    private var clickCount = 1
    private var clickInterface: OnItemClickListener? = null
    private var totalCorrectAns = 0

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
//    fun questionList(list: List<Results>) {
//        differ.submitList(list)
//        notifyDataSetChanged()
//    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewPagerHolder, position: Int) {
        val quize = differ.currentList[position]
        holder.view.txtQuestion.text = quize.question
        holder.view.txtDifficultyType.text = quize.difficulty
        holder.view.txtCategory.text = quize.category

        val queNumber = position + 1
        holder.view.txtAttempt.text =
            queNumber.toString() + " / " + differ.currentList.size.toString()

        answerSet.clear()
        answerSet.add(quize.correct_answer!!)
        answerSet.addAll(quize.incorrect_answers!!)
        holder.view.lylCustomTextview.removeAllViews()
        for (i in 0 until answerSet.size) {
            subLayout = LayoutInflater.from(holder.itemView.context)
                .inflate(R.layout.custom_items_answer, holder.view.lylCustomTextview, false)
            val textAnswer = subLayout.findViewById<TextView>(R.id.txtAns)
            val data = answerSet.elementAt(i)
            textAnswer.text = data
            holder.view.lylCustomTextview.addView(subLayout)
            val quizAnswer = textAnswer.text.toString()
            textAnswer.setOnClickListener {
                clickInterface?.onAnswerClick(quizAnswer, quize)
                getAnswerAction(quizAnswer, textAnswer, quize)
            }
        }

    }

    //Answer click get correct answer perform action
    private fun getAnswerAction(
        quizAnswer: String,
        textAnswer: TextView?,
        quize: Results?
    ) {
            if (quize?.correct_answer == quizAnswer) {
                textAnswer!!.setBackgroundResource(R.color.green)
                textAnswer.setTextColor(Color.WHITE)
                totalCorrectAns = clickCount++
                clickInterface?.onClick(
                    totalCorrectAns.toString(),
                    differ.currentList.size,
                    quize.category!!
                )
            } else {
                textAnswer!!.setBackgroundResource(R.color.red)
                textAnswer.setTextColor(Color.WHITE)
                clickInterface?.onClick(totalCorrectAns.toString(), differ.currentList.size, quize?.category!!)
            }
    }

    fun setItemClick(clickInterface: OnItemClickListener) {
        this.clickInterface = clickInterface
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    class ViewPagerHolder(val view: ItemsQuizBinding) : RecyclerView.ViewHolder(view.root)


    interface OnItemClickListener {
        fun onClick(text: String, totalQue: Int, category: String)
        fun onAnswerClick(quizAnswer: String,quize: Results?)
    }

}
