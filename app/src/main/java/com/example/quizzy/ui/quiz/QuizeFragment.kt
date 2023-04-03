package com.example.quizzy.ui.quiz

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.quizzy.BR
import com.example.quizzy.R
import com.example.quizzy.adapter.QuizAdapter
import com.example.quizzy.base.BaseFragment
import com.example.quizzy.databinding.FragmentQuizeBinding
import com.example.quizzy.ui.settings.SettingsViewModel
import com.example.quizzy.utils.Constant.Companion.SETTIMER
import com.example.quizzy.utils.Constant.Companion.cancel
import com.example.quizzy.utils.Constant.Companion.categorys
import com.example.quizzy.utils.Constant.Companion.ok
import com.example.quizzy.utils.Constant.Companion.result
import com.example.quizzy.utils.Constant.Companion.resultMsg
import com.example.quizzy.utils.Constant.Companion.showPopup
import com.example.quizzy.utils.Constant.Companion.showSnackBar
import com.example.quizzy.utils.Constant.Companion.totalCorrectAnswer
import com.example.quizzy.utils.Constant.Companion.totalQuestion
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple Quiz fragment.
 * In this fragment display question and answer to select and get result.
 */
@AndroidEntryPoint
class QuizeFragment : BaseFragment<FragmentQuizeBinding, QuizeViewModel>() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[QuizeViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arguments = arguments
        if (arguments != null) {
            timeSET = arguments.getString(SETTIMER).toString()
            getBindingClass().txtTimer.visibility = View.VISIBLE
        } else {
            getBindingClass().txtTimer.visibility = View.GONE
        }

        nQuestion = settingsViewModel.getNumberQuestions().toString()
        categoryID = settingsViewModel.getCategorysID().toString()
        difficultyType = settingsViewModel.getDifficultysType().toString()
        questionsType = settingsViewModel.getQuestionssType().toString()
        isChecked = settingsViewModel.getIsChecked().toString()
        Log.d("categoryID", categoryID)
        Log.d("difficultyType", difficultyType)
        Log.d("questionsType", questionsType)
        Log.d("isChecked", isChecked)

        quizQuestionList()
        clickHandle()
    }

    //Display Question in view pager
    private fun quizQuestionList() {
        quizAdapter = QuizAdapter(viewModel, requireContext(), settingsViewModel)
        getBindingClass().viewPager.adapter = quizAdapter
        viewModel.quizList.observe(viewLifecycleOwner) { quize ->
            getBindingClass().progressBar.visibility = View.GONE
            quize.results?.let { quizAdapter.questionList(it) }
            setTimer()
        }

        if (categoryID == "null") {
            viewModel.getQuestionList("10", "", "", "")
        } else {
            viewModel.getQuestionList(nQuestion, categoryID, difficultyType, questionsType)
        }

        quizAdapter.setItemClick(object : QuizAdapter.OnItemClickListener {
            override fun onClick(text: String, total: Int, categorys: String) {
                totalCorrectAns = text
                totalQuestions = total
                category = categorys
            }
        })
    }

    //Display reverse 30 second timer
    @SuppressLint("SetTextI18n")
    private fun setTimer() {
        viewModel.remainingTime.observe(viewLifecycleOwner) { remainingTime ->
            getBindingClass().txtTimer.text = "$remainingTime Sec"
        }
        viewModel.startTimer(30000)
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
            setTimer()
        }

        getBindingClass().lylSubmit.setOnClickListener {
            viewModel.cancelTimer()
            view?.showPopup(result, resultMsg, ok, cancel, {
                val bundle = Bundle()
                bundle.putString(totalCorrectAnswer, totalCorrectAns)
                bundle.putInt(totalQuestion, totalQuestions)
                bundle.putString(categorys, category)
                findNavController().navigate(R.id.resultFragment, bundle)
            }, {
                view?.showSnackBar("Cancel")
            })
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