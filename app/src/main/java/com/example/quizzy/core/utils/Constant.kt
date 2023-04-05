package com.example.quizzy.core.utils

import android.app.AlertDialog
import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.quizzy.R
import com.example.quizzy.databinding.QuizPopupBinding
import com.google.android.material.snackbar.Snackbar

class Constant {

    companion object{

        const val BaseURL = "https://opentdb.com/"

        const val SPLASH_SCREEN_TIMEOUT = 3000L // 3 seconds
        const val SETTIMER = "time"
        const val numberOfQue = "numberOfQue"
        const val categorysId = "categoryID"
        const val difficultySType = "difficultyType"
        const val questionSType = "questionsType"
        const val result = "Result"
        const val resultMsg = "Score updated successfully."
        const val ok = "Okay"
        const val cancel = "Cancel"
        const val isChecked = "false"
        const val totalCorrectAnswer = "totalCorrectAns"
        const val totalQuestion = "totalQuestions"
        const val categorys = "category"
        const val saveTimes = "saveTime"
        const val displayTimer = "DisplayTImer"

        private var dialog: Dialog? = null

        fun View.showSnackBar(message: String?) {
            Snackbar.make(this, message ?: "", Snackbar.LENGTH_SHORT).show()
            val snackbar = Snackbar.make(this, message.toString(), Snackbar.LENGTH_LONG)
            snackbar.view.setBackgroundColor(ContextCompat.getColor(context, R.color.green))
            val snackbarText = snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            snackbarText.setTextColor(ContextCompat.getColor(context, R.color.white))
            snackbar.show()
        }

        fun View.showPopup(title: String, message: String, positiveButton: String,
                           negativeButton: String, onPositiveClick: () -> Unit,
                           onNegativeClick: () -> Unit) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(title)
            builder.setMessage(message)
            builder.setPositiveButton(positiveButton) { dialog, which ->
                onPositiveClick.invoke()
            }
            builder.setNegativeButton(negativeButton) { dialog, which ->
                onNegativeClick.invoke()
            }
            val dialog = builder.create()
            dialog.show()
        }

        fun View.showDialog(totalCorrectAns:String,
                            totalQuestion:String,
                            categoryType:String,
                            saveTime:String,
                            timeSET:String){
            val binding = DataBindingUtil.inflate<QuizPopupBinding>(
                LayoutInflater.from(context),
                R.layout.quiz_popup,
                null,
                false
            )
            binding.txtCategoryType.text = categoryType
            binding.txtScored.text = totalCorrectAns
            binding.txtTotalQue.text = totalQuestion
            binding.txtQuizType.text = categoryType
            binding.txtTakenTime.text = saveTime + "min"

            binding.txtStart.setOnClickListener {
                rootView?.showSnackBar("Click")
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