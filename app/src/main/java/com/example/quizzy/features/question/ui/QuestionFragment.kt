package com.example.quizzy.features.question.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.quizzy.BR
import com.example.quizzy.R
import com.example.quizzy.core.base.BaseFragment
import com.example.quizzy.core.utils.Constant.Companion.NO_INTERNET
import com.example.quizzy.core.utils.Constant.Companion.NO_RECORD
import com.example.quizzy.core.utils.Constant.Companion.SELECT_ANSWER
import com.example.quizzy.core.utils.Constant.Companion.SET_TIMER
import com.example.quizzy.core.utils.Constant.Companion.showDialog
import com.example.quizzy.core.utils.Constant.Companion.showSnackBar
import com.example.quizzy.core.utils.Constant.Companion.showSnackRedBar
import com.example.quizzy.core.utils.Status
import com.example.quizzy.databinding.FragmentQuizeBinding
import com.example.quizzy.features.settings.ui.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple Quiz fragment.
 * In this fragment display question and answer to select and get result.
 */
@AndroidEntryPoint
class QuestionFragment : BaseFragment<FragmentQuizeBinding, QuestionViewModel>() {

    private lateinit var questionAdapter: QuestionAdapter
    private lateinit var timeSet: String
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
        viewModel = ViewModelProvider(this)[QuestionViewModel::class.java]
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arguments = arguments
        if (arguments != null) {
            timeSet = arguments.getString(SET_TIMER).toString()
            getBindingClass().txtTimer.visibility = View.VISIBLE

        } else {
            timeSet = arguments?.getString(SET_TIMER).toString()
            getBindingClass().txtTimer.visibility = View.GONE
        }

        nQuestion = settingsViewModel.getsaveSettingData().nQuestion
        categoryID = settingsViewModel.getsaveSettingData().categoryID
        difficultyType = settingsViewModel.getsaveSettingData().difficultyType
        questionsType = settingsViewModel.getsaveSettingData().questionsType
        isChecked = settingsViewModel.getsaveSettingData().isCheck.toString()

        context?.let { viewModel.checkInternetConnection(it) }
        viewModel.isConnected.observe(viewLifecycleOwner) { isConnected->
            if (isConnected){
                getBindingClass().rlQuiz.visibility = View.VISIBLE
                getBindingClass().imgNoInternet.visibility = View.GONE
                quizQuestionList()
            }else{
                view.showSnackRedBar(NO_INTERNET)
                getBindingClass().rlQuiz.visibility = View.GONE
                getBindingClass().imgNoInternet.visibility = View.VISIBLE
            }
        }
        buttonClickHandler()
    }

    //Display Question in view pager
    private fun quizQuestionList() {
        questionAdapter = QuestionAdapter(viewModel, requireContext(), settingsViewModel)
        getBindingClass().viewPager.adapter = questionAdapter
        viewModel.questionResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    getBindingClass().progressBar.visibility = View.GONE
                    getBindingClass().viewPager.visibility = View.VISIBLE
                    it.data.let { res ->
                        when (res?.response_code) {
                            0 -> {
                                res.results?.let { it1 -> questionAdapter.questionList(it1) }
                                setTimer()
                            }
                            1 -> {
                                view?.showSnackBar(NO_RECORD)
                                getBindingClass().imgNoRecord.visibility = View.VISIBLE
                                getBindingClass().rlQuiz.visibility = View.GONE
                            }
                            2 -> {
                                view?.showSnackBar(NO_RECORD)
                                getBindingClass().imgNoRecord.visibility = View.VISIBLE
                                getBindingClass().rlQuiz.visibility = View.GONE
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
        }

        if (categoryID == "") {
            viewModel.getQuestionList("10", "", "", "")
        } else {
            viewModel.getQuestionList(nQuestion, categoryID, difficultyType, questionsType)
        }

        questionAdapter.setItemClick(object : QuestionAdapter.OnItemClickListener {
            override fun onClick(text: String, totalQue: Int, categorysType: String) {
                isClick = true
                totalCorrectAns = text
                totalQuestions = totalQue
                category = categorysType
            }
        })
    }

    //Display reverse 30 second timer
    private fun setTimer() {
        viewModel.currentTime.observe(viewLifecycleOwner) { timeLeft ->

            getBindingClass().txtTimer.text = timeLeft

        }
        viewModel.startTimer()
    }

    //Cancel timer
    override fun onDestroy() {
        super.onDestroy()
        viewModel.cancelTimer()
    }

    //Handle click for next question display
    private fun buttonClickHandler() {
        getBindingClass().imgNext.setOnClickListener {
            nextDataShow()
        }

        getBindingClass().lylSubmit.setOnClickListener {
            if (!isClick){
                view?.showSnackBar(SELECT_ANSWER)
            }else{
                viewModel.cancelTimer()
                val saveTime = getBindingClass().txtTimer.text
                if (nQuestion == ""){
                    view?.showDialog(totalCorrectAns, "10", category, saveTime.toString(), timeSet, difficultyType)
                }else{
                    view?.showDialog(totalCorrectAns, nQuestion, category, saveTime.toString(), timeSet, difficultyType)
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