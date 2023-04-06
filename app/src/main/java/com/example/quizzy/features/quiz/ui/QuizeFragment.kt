package com.example.quizzy.features.quiz.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.quizzy.BR
import com.example.quizzy.R
import com.example.quizzy.core.base.BaseFragment
import com.example.quizzy.core.utils.Constant.Companion.SETTIMER
import com.example.quizzy.core.utils.Constant.Companion.selectAnswer
import com.example.quizzy.core.utils.Constant.Companion.showDialog
import com.example.quizzy.core.utils.Constant.Companion.showSnackBar
import com.example.quizzy.core.utils.Status
import com.example.quizzy.databinding.FragmentQuizeBinding
import com.example.quizzy.features.settings.ui.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple Quiz fragment.
 * In this fragment display question and answer to select and get result.
 */
@AndroidEntryPoint
class QuizeFragment : BaseFragment<FragmentQuizeBinding, QuizViewModel>() {

    private lateinit var quizAdapter: QuizAdapter
    private lateinit var timeSET: String
    private val settingsViewModel: SettingsViewModel by viewModels()

    var totalQuestions: Int = 0
    private lateinit var category: String
    private lateinit var totalCorrectAns: String
    private lateinit var nQuestion: String
    private lateinit var categoryID: String
    private lateinit var difficultyType: String
    private lateinit var questionsType: String
    private lateinit var isChecked: String
    var isClick :Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[QuizViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arguments = arguments
        if (arguments != null) {
            timeSET = arguments.getString(SETTIMER).toString()
            getBindingClass().txtTimer.visibility = View.VISIBLE

        } else {
            timeSET = arguments?.getString(SETTIMER).toString()
            getBindingClass().txtTimer.visibility = View.GONE
        }


        nQuestion = settingsViewModel.getNumberQuestions().toString()
        categoryID = settingsViewModel.getCategorysID().toString()
        difficultyType = settingsViewModel.getDifficultysType().toString()
        questionsType = settingsViewModel.getQuestionssType().toString()
        isChecked = settingsViewModel.getIsChecked().toString()

        quizQuestionList()
        clickHandle()
    }

    //Display Question in view pager
    private fun quizQuestionList() {
        quizAdapter = QuizAdapter(viewModel, requireContext(), settingsViewModel)
        getBindingClass().viewPager.adapter = quizAdapter
        viewModel.questionResponse.observe(viewLifecycleOwner, Observer  {
            when(it.status){
                Status.SUCCESS -> {
                    getBindingClass().progressBar.visibility = View.GONE
                    getBindingClass().viewPager.visibility = View.VISIBLE
                    it.data.let {res->
                        when (res?.response_code) {
                            0 -> {
                                res.results?.let { it1 -> quizAdapter.questionList(it1) }
                                setTimer()
                            }
                            1 -> {
                                view?.showSnackBar(it.message)
                            }
                            2 -> {
                                view?.showSnackBar(it.message)
                            }
                            else -> {
                                view?.showSnackBar(it.message)
                            }
                        }
                    }
                }
                Status.LOADING -> {
                    getBindingClass().progressBar.visibility = View.VISIBLE
                    getBindingClass().viewPager.visibility = View.GONE
                }
                Status.ERROR -> {
                    getBindingClass().progressBar.visibility = View.GONE
                    getBindingClass().viewPager.visibility = View.VISIBLE
                    view?.showSnackBar(it.message)
                }
            }
        })

        if (categoryID == "null") {
            viewModel.getQuestionList("10", "", "", "")
        } else {
            viewModel.getQuestionList(nQuestion, categoryID, difficultyType, questionsType)
        }

        quizAdapter.setItemClick(object : QuizAdapter.OnItemClickListener {
            override fun onClick(correctAns: String, totalQue: Int, categorysType: String) {
                isClick = true
                totalCorrectAns = correctAns
                totalQuestions = totalQue
                category = categorysType
            }
        })
    }

    //Display reverse 30 second timer
    private fun setTimer() {
        viewModel.currentTime.observe(viewLifecycleOwner, Observer { timeLeft ->

            getBindingClass().txtTimer.text = timeLeft

        })
        viewModel.startTimer()
    }

    //Cancel timer
    override fun onDestroy() {
        super.onDestroy()
        viewModel.cancelTimer()
    }

    //Handle click for next question display
    private fun clickHandle() {
        getBindingClass().imgNext.setOnClickListener {
            nextDataShow()
        }

        getBindingClass().lylSubmit.setOnClickListener {
            if (!isClick){
                view?.showSnackBar(selectAnswer)
            }else{
                viewModel.cancelTimer()
                val saveTime = getBindingClass().txtTimer.text
                if (nQuestion == "null"){
                    view?.showDialog(totalCorrectAns, "10", category, saveTime.toString(), timeSET)
                }else{
                    view?.showDialog(totalCorrectAns, nQuestion, category, saveTime.toString(), timeSET)
                }
            }
        }
    }

    //Button click display next data.
    private fun nextDataShow() {
        viewModel.currentPosition.observe(viewLifecycleOwner) { position ->
            getBindingClass().viewPager.currentItem = position
        }
        viewModel.onButtonClicked()
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getBindingClass(): FragmentQuizeBinding {
        return (getViewDataBinding() as FragmentQuizeBinding)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_quize
    }

}