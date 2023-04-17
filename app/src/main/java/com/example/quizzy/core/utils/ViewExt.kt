package com.example.quizzy.core.utils

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.quizzy.R
import com.example.quizzy.databinding.QuizPopupBinding
import com.google.android.material.snackbar.Snackbar

class ViewExt {

    companion object{
        private var dialog: Dialog? = null

        fun View.showSnackBar(message: String?) {
            Snackbar.make(this, message ?: "", Snackbar.LENGTH_SHORT).show()
            val snackBar = Snackbar.make(this, message.toString(), Snackbar.LENGTH_LONG)
            if (message != Constant.NO_RECORD) {
                snackBar.view.setBackgroundColor(ContextCompat.getColor(context, R.color.red))
            } else {
                snackBar.view.setBackgroundColor(ContextCompat.getColor(context, R.color.green))
            }
            snackBar.show()
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
                bundle.putString(Constant.TOTAL_CORRECT_ANS, totalCorrectAns)
                bundle.putString(Constant.DIFFICULTY_TYPE, difficultyType)
                bundle.putString(Constant.TOTAL_QUESTIONS, totalQuestions)
                bundle.putString(Constant.CATEGORYS, categoryType)
                bundle.putString(Constant.SAVE_TIMES, saveTime)
                bundle.putString(Constant.DISPLAY_TIME, timeSET)
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