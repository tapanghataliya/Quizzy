package com.example.quizzy.core.utils

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.quizzy.R
import com.example.quizzy.databinding.QuizPopupBinding
import com.google.android.material.snackbar.Snackbar

class Constant {

    companion object{

        const val BaseURL = "https://opentdb.com/"

        const val SPLASH_SCREEN_TIMEOUT = 3000L // 3 seconds

        const val SET_TIMER = "time"
        const val NUMBER_QUESTION = "numberOfQue"
        const val CATEGORY_ID = "categoryID"
        const val CATEGORY_NAME = "categoryName"
        const val DIFFICULTY_TYPE = "difficultyType"
        const val QUESTIONS_TYPE = "questionsType"
        const val ISCHECKED = "false"
        const val TOTAL_CORRECT_ANS = "totalCorrectAns"
        const val TOTAL_QUESTIONS = "totalQuestions"
        const val CATEGORYS = "category"
        const val SAVE_TIMES = "saveTime"
        const val DISPLAY_TIME = "DisplayTimer"
        const val SELECT_ANSWER = "Please select answer"
        const val NO_INTERNET = "No internet connection"
        const val NO_RECORD = "No Record Found"

        private var dialog: Dialog? = null

        fun View.showSnackBar(message: String?) {
            Snackbar.make(this, message ?: "", Snackbar.LENGTH_SHORT).show()
            val snackbar = Snackbar.make(this, message.toString(), Snackbar.LENGTH_LONG)
            snackbar.view.setBackgroundColor(ContextCompat.getColor(context, R.color.green))
            val snackbarText = snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            snackbarText.setTextColor(ContextCompat.getColor(context, R.color.white))
            snackbar.show()
        }

        fun View.showSnackRedBar(message: String?) {
            Snackbar.make(this, message ?: "", Snackbar.LENGTH_LONG).show()
            val snackbar = Snackbar.make(this, message.toString(), Snackbar.LENGTH_LONG)
            snackbar.view.setBackgroundColor(ContextCompat.getColor(context, R.color.red))
            val snackbarText = snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            snackbarText.setTextColor(ContextCompat.getColor(context, R.color.white))
            snackbar.show()
        }

        @SuppressLint("SetTextI18n")
        fun View.showDialog(totalCorrectAns:String,
                            totalQuestions:String,
                            categoryType:String,
                            saveTime:String,
                            timeSET:String,
                            difficultyType:String){
            val binding = DataBindingUtil.inflate<QuizPopupBinding>(
                LayoutInflater.from(context),
                R.layout.quiz_popup,
                null,
                false
            )
            if (timeSET =="null"){
                binding.lylTotalTime.visibility = View.GONE
                binding.lylTakenTime.visibility = View.GONE
            }

            if (difficultyType == "")binding.lylDifficultyType.visibility = View.GONE else binding.lylDifficultyType.visibility = View.VISIBLE
            binding.txtCategoryType.text = categoryType
            binding.txtDifficultyType.text = difficultyType
            binding.txtScored.text = totalCorrectAns
            binding.txtTotalQue.text = totalQuestions
            binding.txtQuizType.text = categoryType
            binding.txtTakenTime.text = saveTime + "min"

            binding.txtStart.setOnClickListener {
                val bundle = Bundle()
                bundle.putString(TOTAL_CORRECT_ANS, totalCorrectAns)
                bundle.putString(DIFFICULTY_TYPE, difficultyType)
                bundle.putString(TOTAL_QUESTIONS, totalQuestions)
                bundle.putString(CATEGORYS, categoryType)
                bundle.putString(SAVE_TIMES, saveTime)
                bundle.putString(DISPLAY_TIME, timeSET)
                findNavController().navigate(R.id.resultFragment, bundle)
                dialog?.cancel()
            }
            dialog = Dialog(context).apply {
                setContentView(binding.root)
                setCancelable(false)
                window!!.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                show()
            }
        }
    }

}