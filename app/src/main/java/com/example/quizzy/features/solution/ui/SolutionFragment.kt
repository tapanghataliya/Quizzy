package com.example.quizzy.features.solution.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.quizzy.BR
import com.example.quizzy.R
import com.example.quizzy.core.base.BaseFragment
import com.example.quizzy.core.utils.Status
import com.example.quizzy.core.utils.ViewExt.Companion.showSnackBar
import com.example.quizzy.databinding.FragmentSolutionBinding
import com.example.quizzy.features.settings.ui.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SolutionFragment: BaseFragment<FragmentSolutionBinding, SolutionViewModel>() {

    private val settingsViewModel: SettingsViewModel by viewModels()
    private lateinit var solutionAdapter: SolutionAdapter

    private lateinit var nQuestion: String
    private lateinit var categoryID: String
    private lateinit var difficultyType: String
    private lateinit var questionsType: String
    private lateinit var isChecked: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[SolutionViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nQuestion = settingsViewModel.getsaveSettingData().nQuestion
        categoryID = settingsViewModel.getsaveSettingData().categoryID
        difficultyType = settingsViewModel.getsaveSettingData().difficultyType
        questionsType = settingsViewModel.getsaveSettingData().questionsType
        isChecked = settingsViewModel.getsaveSettingData().isCheck.toString()

        solutionList()
        clickHandle()
    }

    private fun solutionList() {
        solutionAdapter = SolutionAdapter()
        getBindingClass().viewPager.adapter = solutionAdapter
        viewModel.solutionResponse.observe(viewLifecycleOwner)  {
            when(it.status){
                Status.SUCCESS -> {
                    getBindingClass().progressBar.visibility = View.GONE
                    getBindingClass().viewPager.visibility = View.VISIBLE
                    it.data.let {res->
                        when (res?.response_code) {
                            0 -> {
                                res.results?.let { it1 -> solutionAdapter.questionList(it1) }
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
        }
        if (categoryID == "null") {
            viewModel.getQuestionList("10", "", "", "")
        } else {
            viewModel.getQuestionList(nQuestion, categoryID, difficultyType, questionsType)
        }

    }

    private fun clickHandle() {
        getBindingClass().imgNext.setOnClickListener {
            nextDataShow()
        }
    }

    private fun nextDataShow() {
        viewModel.currentPosition.observe(viewLifecycleOwner) { position ->
            getBindingClass().viewPager.currentItem = position
        }
        viewModel.onButtonClicked()
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getBindingClass(): FragmentSolutionBinding {
        return (getViewDataBinding() as FragmentSolutionBinding)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_solution
    }

    override fun onDestroy() {
        super.onDestroy()
        getBindingClass().unbind()
    }
}