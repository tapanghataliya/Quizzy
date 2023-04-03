package com.example.quizzy.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
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
import java.util.*


@Suppress("UNREACHABLE_CODE")
class QuizAdapter(
    private val quizeViewModel: QuizeViewModel,
    private val context: Context,
    private val settingsViewModel: SettingsViewModel
) : RecyclerView.Adapter<QuizAdapter.ViewPagerHolder>() {

    private var addAnsList = mutableListOf<String>()
//    var isButtonClicked = false
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

        val myHashSet = HashSet<String>()
        myHashSet.clear()
        myHashSet.add(quize.correct_answer!!)
        myHashSet.addAll(quize.incorrect_answers!!)
        Log.d("myHashSet", myHashSet.toString())

        for (i in 0 until myHashSet.size){

            val subLayout = LayoutInflater.from(holder.itemView.context)
                .inflate(R.layout.custom_items_answer, holder.view.lylCustomTextview, false)
            val textAnswer = subLayout.findViewById<TextView>(R.id.txtAns)
            val data = myHashSet.elementAt(i)
            textAnswer.text = data
            holder.view.lylCustomTextview.addView(subLayout)
        }

//        addAnsList.clear()
//        addAnsList.add(quize.correct_answer!!)
//        for (i in quize.incorrect_answers!!) {
//            addAnsList.add(i)
//        }
//        addAnsList.shuffle()

//        for (i in 0 until addAnsList.size) {
//            // Add new views dynamically here
//            // Programmatically inflate the sub-layout
//            val subLayout = LayoutInflater.from(holder.itemView.context)
//                .inflate(R.layout.custom_items_answer, holder.view.lylCustomTextview, false)
//            val textAnswer = subLayout.findViewById<TextView>(R.id.txtAns)
//            textAnswer.text = addAnsList[i]
//            holder.view.lylCustomTextview.addView(subLayout)
//
//            textAnswer.setOnClickListener {
//                if (settingsViewModel.getIsChecked()){
//                    val quizAnswer = textAnswer.text.toString()
//                    if (quize.correct_answer == quizAnswer) {
//                        textAnswer.setBackgroundResource(R.color.green)
//                        textAnswer.setTextColor(Color.WHITE)
//                        val totalCorrectAns = clickCount++
//                        Log.d("CorrectAns", totalCorrectAns.toString())
//                        clickInterface?.onClick(totalCorrectAns.toString(), differ.currentList.size,
//                            quize.category!!
//                        )
//                    } else {
//                        textAnswer.setBackgroundResource(R.color.white)
//                        quizeViewModel.onIncorrectAnswerSelected()
//                        quizeViewModel.vibrationDurations.value = 50
//                        clickInterface?.onClick("0", differ.currentList.size,quize.category!!)
//                    }
//                }else{
//                    val quizAnswer = textAnswer.text.toString()
//                    if (quize.correct_answer == quizAnswer) {
//                        textAnswer.setBackgroundResource(R.color.green)
//                        textAnswer.setTextColor(Color.WHITE)
//                        val totalCorrectAns = clickCount++
//                        clickInterface?.onClick(totalCorrectAns.toString(), differ.currentList.size,quize.category!!)
//                    } else {
//                        textAnswer.setBackgroundResource(R.color.white)
//                        clickInterface?.onClick("0", differ.currentList.size,quize.category!!)
//                    }
//                }
//
//            }
//
////            if (!isButtonClicked){
////                isButtonClicked = true
////                val data = textAnswer.text.toString()
////                if (quize.correct_answer == data){
////                    textAnswer.setBackgroundResource(R.color.green)
////                    textAnswer.setTextColor(Color.WHITE)
////                }else{
////                    textAnswer.setBackgroundResource(R.color.red)
////                    textAnswer.setTextColor(Color.WHITE)
////                }
////            }
//
//        }
    }
    fun setItemClick(clickInterface: OnItemClickListener) {
        this.clickInterface = clickInterface
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    class ViewPagerHolder(val view: ItemsQuizBinding) : RecyclerView.ViewHolder(view.root)


    interface OnItemClickListener {
        fun onClick(text: String, totalQue: Int, category:String)
    }
}
