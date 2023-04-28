package com.example.quizzy.features.question.presentation.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.quizzy.BR
import com.example.quizzy.R
import com.example.quizzy.core.base.BaseFragment
import com.example.quizzy.core.utils.Constant
import com.example.quizzy.core.utils.Status
import com.example.quizzy.core.utils.ViewExt.Companion.showDialog
import com.example.quizzy.core.utils.ViewExt.Companion.showSnackBar
import com.example.quizzy.databinding.FragmentQuizeBinding
import com.example.quizzy.features.question.data.model.Results
import com.example.quizzy.features.question.presentation.adapter.QuestionAdapter
import com.example.quizzy.features.question.presentation.viewmodel.QuestionsViewModel
import com.example.quizzy.features.settings.presentation.viewmodel.SettingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuestionsFragment : BaseFragment<FragmentQuizeBinding, QuestionsViewModel>(){

    private lateinit var questionAdapter: QuestionAdapter
    private lateinit var timeSet: String
    private val settingsViewModel: SettingViewModel by viewModels()

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
        viewModel = ViewModelProvider(this)[QuestionsViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arguments = arguments
        if (arguments != null) {
            timeSet = arguments.getString(Constant.SET_TIMER).toString()
            getBindingClass().txtTimer.visibility = View.VISIBLE

        } else {
            timeSet = arguments?.getString(Constant.SET_TIMER).toString()
            getBindingClass().txtTimer.visibility = View.GONE
        }

        nQuestion = settingsViewModel.getsaveSettingData().nQuestion
        categoryID = settingsViewModel.getsaveSettingData().categoryID
        difficultyType = settingsViewModel.getsaveSettingData().difficultyType
        questionsType = settingsViewModel.getsaveSettingData().questionsType
        isChecked = settingsViewModel.getsaveSettingData().isCheck.toString()

        quizQuestionsList()
        buttonClickHandler()

    }

    //Display Question in view pager
    private fun quizQuestionsList() {
        questionAdapter = QuestionAdapter()
        getBindingClass().viewPager.adapter = questionAdapter
        getBindingClass().viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == getBindingClass().viewPager.adapter?.itemCount?.minus(1)){
                    getBindingClass().imgNext.visibility = View.GONE
                }else{
                    getBindingClass().imgNext.visibility = View.VISIBLE
                }
            }
        })

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
                                view?.showSnackBar(Constant.NO_RECORD)
                                getBindingClass().imgNoRecord.visibility = View.VISIBLE
                                getBindingClass().rlQuiz.visibility = View.GONE
                            }
                            2 -> {
                                view?.showSnackBar(Constant.NO_RECORD)
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
                    view?.showSnackBar(Constant.NO_INTERNET)
                    getBindingClass().rlQuiz.visibility = View.GONE
                    getBindingClass().imgNoInternet.visibility = View.VISIBLE
                }
            }
        }

        if (categoryID == "") {
            viewModel.getQuestionsList("10", "", "", "")
        } else {
            viewModel.getQuestionsList(nQuestion, categoryID, difficultyType, questionsType)
        }

        questionAdapter.setItemClick(object : QuestionAdapter.OnItemClickListener {
            override fun onClick(text: String, totalQue: Int, categorysType: String) {
                isClick = true
                totalCorrectAns = text
                totalQuestions = totalQue
                category = categorysType
            }

            override fun onAnswerClick(quizAnswer: String, quize: Results?) {
                if (settingsViewModel.getsaveSettingData().isCheck){
                    if (quize?.correct_answer == quizAnswer){
                        viewModel.correctSoundPlayback()
                    }else{
                        viewModel.wrongSoundPlayback()
                        context?.let { viewModel.onClickVibrat(it) }
                    }
                    moveToNextQuestion()
                }else{
                    moveToNextQuestion()
                }
            }
        })
    }

    private fun moveToNextQuestion() {
        viewModel.currentPage.observe(viewLifecycleOwner) { position ->
            getBindingClass().viewPager.currentItem = position
        }
        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.nextQuestion()
        }, 1000)
    }

    //Handle click for next question display
    private fun buttonClickHandler() {
        getBindingClass().imgNext.setOnClickListener {
            nextDataShow()
        }

        getBindingClass().lylSubmit.setOnClickListener {
            if (!isClick){
                view?.showSnackBar(Constant.SELECT_ANSWER)
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

    //Display reverse 30 second timer
    private fun setTimer() {
        viewModel.currentTime.observe(viewLifecycleOwner) { timeLeft ->

            getBindingClass().txtTimer.text = timeLeft

        }
        viewModel.startTimer()
    }

    //Button click display next data.
    private fun nextDataShow() {
        viewModel.currentPosition.observe(viewLifecycleOwner) { position ->
            getBindingClass().viewPager.currentItem = position
        }
        viewModel.onButtonClicked()
    }

    //Cancel timer and unbinding view
    override fun onDestroy() {
        super.onDestroy()
        viewModel.cancelTimer()
        getBindingClass().unbind()
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