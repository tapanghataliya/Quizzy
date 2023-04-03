package com.example.quizzy.ui.solution

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.quizzy.BR
import com.example.quizzy.R
import com.example.quizzy.adapter.SolutionAdapter
import com.example.quizzy.base.BaseFragment
import com.example.quizzy.databinding.FragmentSolutionBinding
import com.example.quizzy.ui.settings.SettingsViewModel
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

        nQuestion = settingsViewModel.getNumberQuestions().toString()
        categoryID = settingsViewModel.getCategorysID().toString()
        difficultyType = settingsViewModel.getDifficultysType().toString()
        questionsType = settingsViewModel.getQuestionssType().toString()
        isChecked = settingsViewModel.getIsChecked().toString()

        solutionList()
        clickHandle()
    }

    private fun solutionList() {
        solutionAdapter = SolutionAdapter(viewModel, requireContext(), settingsViewModel)
        getBindingClass().viewPager.adapter = solutionAdapter
        viewModel.solutionList.observe(viewLifecycleOwner) { quize ->
            getBindingClass().progressBar.visibility = View.GONE
            quize.results?.let { solutionAdapter.questionList(it) }
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
}