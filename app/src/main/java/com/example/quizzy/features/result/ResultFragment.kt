package com.example.quizzy.features.result

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.quizzy.BR
import com.example.quizzy.R
import com.example.quizzy.core.base.BaseFragment
import com.example.quizzy.core.utils.Constant.Companion.categorys
import com.example.quizzy.core.utils.Constant.Companion.displayTimer
import com.example.quizzy.core.utils.Constant.Companion.saveTimes
import com.example.quizzy.core.utils.Constant.Companion.totalCorrectAnswer
import com.example.quizzy.core.utils.Constant.Companion.totalQuestion
import com.example.quizzy.databinding.FragmentResultBinding

class ResultFragment : BaseFragment<FragmentResultBinding, ResultViewModel>() {

    private lateinit var totalQuestions: String
    private lateinit var totalCorrectAns: String
    private lateinit var categoryType: String
    private lateinit var saveTime: String
    private lateinit var timeSET: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ResultViewModel::class.java]
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arguments = arguments
        if (arguments != null){
            totalQuestions = arguments.getString(totalQuestion).toString()
            totalCorrectAns = arguments.getString(totalCorrectAnswer).toString()
            categoryType = arguments.getString(categorys).toString()
            saveTime = arguments.getString(saveTimes).toString()
            timeSET = arguments.getString(displayTimer).toString()
            if (timeSET == "null"){
                getBindingClass().cardTime.visibility = View.GONE
                getBindingClass().cardTimeTaken.visibility = View.GONE
            }

            getBindingClass().txtScored.text = totalCorrectAns
            getBindingClass().txtTotalQue.text = totalQuestions
            getBindingClass().txtQuizType.text = categoryType
            getBindingClass().txtTakenTime.text = saveTime + "min"
        }

        clickHandle()
    }

    private fun clickHandle() {
        getBindingClass().txtSolution.setOnClickListener {
            findNavController().navigate(R.id.solutionFragment)
        }
        getBindingClass().txtReAttempt.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getBindingClass(): FragmentResultBinding {
        return (getViewDataBinding() as FragmentResultBinding)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_result
    }


}