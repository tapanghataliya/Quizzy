package com.example.quizzy.ui.result

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.quizzy.BR
import com.example.quizzy.R
import com.example.quizzy.base.BaseFragment
import com.example.quizzy.databinding.FragmentResultBinding
import com.example.quizzy.databinding.FragmentSettingsBinding
import com.example.quizzy.ui.quiz.QuizeViewModel
import com.example.quizzy.ui.settings.SettingsViewModel
import com.example.quizzy.utils.Constant.Companion.categorys
import com.example.quizzy.utils.Constant.Companion.saveTimes
import com.example.quizzy.utils.Constant.Companion.totalCorrectAnswer
import com.example.quizzy.utils.Constant.Companion.totalQuestion

class ResultFragment : BaseFragment<FragmentResultBinding, ResultViewModel>() {

    var totalQuestions: Int = 0
    private lateinit var totalCorrectAns: String
    private lateinit var categoryType: String
    private lateinit var saveTime: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ResultViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arguments = arguments
        if (arguments != null){
            totalQuestions = arguments.getInt(totalQuestion)
            totalCorrectAns = arguments.getString(totalCorrectAnswer).toString()
            categoryType = arguments.getString(categorys).toString()
            saveTime = arguments.getString(saveTimes).toString()

            getBindingClass().txtScored.text = totalCorrectAns
            getBindingClass().txtTotalQue.text = totalQuestions.toString()
            getBindingClass().txtQuizType.text = categoryType
            getBindingClass().txtTakenTime.text = saveTime + "min"
        }

        clickHandle()
    }

    private fun clickHandle() {
        getBindingClass().txtRefresh.setOnClickListener {  }
        getBindingClass().txtSolution.setOnClickListener {
            findNavController().navigate(R.id.solutionFragment)
        }
        getBindingClass().txtReAttempt.setOnClickListener {  }
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