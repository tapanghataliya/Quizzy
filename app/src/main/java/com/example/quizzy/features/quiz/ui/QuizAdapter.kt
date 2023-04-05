package com.example.quizzy.features.quiz.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.quizzy.R
import com.example.quizzy.databinding.ItemsQuizBinding
import com.example.quizzy.features.quiz.data.Results
import com.example.quizzy.features.settings.ui.SettingsViewModel
import java.util.*


@Suppress("UNREACHABLE_CODE")
class QuizAdapter(
    private val quizeViewModel: QuizViewModel,
    private val context: Context,
    private val settingsViewModel: SettingsViewModel
) : RecyclerView.Adapter<QuizAdapter.ViewPagerHolder>() {

    val answerSet = HashSet<String>()
    var clickCount = 1
    private var clickInterface: OnItemClickListener? = null

    init {
        quizeViewModel.incorrectAnswerSound.observeForever {
            it?.let { sound ->
                val mediaPlayer = MediaPlayer.create(context, sound)
                mediaPlayer.start()
            }
        }

        quizeViewModel.vibrationDuration.observeForever {
            it?.let { duration ->
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(
                            duration,
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )
                } else {
                    vibrator.vibrate(duration)
                }
            }
        }
    }

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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewPagerHolder, position: Int) {
        val quize = differ.currentList[position]
        holder.view.txtQuestion.text = quize.question
        holder.view.txtCategory.text = quize.category

        val queNumber = position + 1
        holder.view.txtAttempt.text =
            queNumber.toString() + " / " + differ.currentList.size.toString()


        answerSet.clear()
        answerSet.add(quize.correct_answer!!)
        answerSet.addAll(quize.incorrect_answers!!)
        for (i in 0 until answerSet.size) {

            val subLayout = LayoutInflater.from(holder.itemView.context)
                .inflate(R.layout.custom_items_answer, holder.view.lylCustomTextview, false)
            val textAnswer = subLayout.findViewById<TextView>(R.id.txtAns)
            val data = answerSet.elementAt(i)
            textAnswer.text = data
            holder.view.lylCustomTextview.addView(subLayout)
            val quizAnswer = textAnswer.text.toString()
            textAnswer.setOnClickListener {
                getAnswerAction(quizAnswer, textAnswer, quize)
            }
        }

    }

    //Answer click get correct answer perform action
    private fun getAnswerAction(quizAnswer: String, textAnswer: TextView?, quize: Results?) {
        if (settingsViewModel.getIsChecked()) {
            if (quize?.correct_answer == quizAnswer) {
                textAnswer!!.setBackgroundResource(R.color.green)
                textAnswer.setTextColor(Color.WHITE)
                val totalCorrectAns = clickCount++
                clickInterface?.onClick(
                    totalCorrectAns.toString(),
                    differ.currentList.size,
                    quize.category!!
                )
            } else {
                textAnswer!!.setBackgroundResource(R.color.white)
                quizeViewModel.onIncorrectAnswerSelected()
                quizeViewModel.vibrationDurations.value = 50
                clickInterface?.onClick("0", differ.currentList.size, quize?.category!!)
            }
        } else {
            if (quize?.correct_answer == quizAnswer) {
                textAnswer!!.setBackgroundResource(R.color.green)
                textAnswer.setTextColor(Color.WHITE)
                val totalCorrectAns = clickCount++
                clickInterface?.onClick(
                    totalCorrectAns.toString(),
                    differ.currentList.size,
                    quize.category!!
                )
            } else {
                textAnswer!!.setBackgroundResource(R.color.white)
                clickInterface?.onClick("0", differ.currentList.size, quize?.category!!)
            }
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
    }
}
