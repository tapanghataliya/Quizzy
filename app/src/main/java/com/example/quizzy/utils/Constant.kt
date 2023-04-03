package com.example.quizzy.utils

import android.app.AlertDialog
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.quizzy.R
import com.google.android.material.snackbar.Snackbar

class Constant {

    companion object{

        const val BaseURL = "https://opentdb.com/"

        const val SETTIMER = "time"
        const val numberOfQue = "numberOfQue"
        const val categorysId = "categoryID"
        const val difficultySType = "difficultyType"
        const val questionSType = "questionsType"
        const val exitApplication = "Press again to exit"
        const val result = "Result"
        const val resultMsg = "Score updated successfully."
        const val ok = "Okay"
        const val cancel = "Cancel"
        const val isChecked = "false"
        const val totalCorrectAnswer = "totalCorrectAns"
        const val totalQuestion = "totalQuestions"
        const val categorys = "category"

        fun View.showSnackBar(message: String?) {
            Snackbar.make(this, message ?: "", Snackbar.LENGTH_SHORT).show()
            val snackbar = Snackbar.make(this, message.toString(), Snackbar.LENGTH_LONG)
            snackbar.view.setBackgroundColor(ContextCompat.getColor(context, R.color.green))
            val snackbarText = snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            snackbarText.setTextColor(ContextCompat.getColor(context, R.color.white))
            snackbar.show()
        }

        fun View.showPopup(title: String, message: String, positiveButton: String, negativeButton: String, onPositiveClick: () -> Unit, onNegativeClick: () -> Unit) {
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
    }
}