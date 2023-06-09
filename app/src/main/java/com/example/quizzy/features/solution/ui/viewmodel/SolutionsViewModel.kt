package com.example.quizzy.features.solution.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.quizzy.core.base.BaseNavigator
import com.example.quizzy.core.base.BaseViewModel
import com.example.quizzy.core.utils.Resource
import com.example.quizzy.features.question.data.model.QuestionList
import com.example.quizzy.features.question.domain.model.QuestionslistDomain
import com.example.quizzy.features.solution.domain.usecase.SolutionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SolutionsViewModel @Inject constructor(
    private val solutionsUseCase: SolutionsUseCase
) : BaseViewModel<BaseNavigator>() {

    private val _solutionRes = MutableLiveData<Resource<QuestionslistDomain>>()
    val solutionResponse: LiveData<Resource<QuestionslistDomain>> = _solutionRes

    private val _currentPosition = MutableLiveData<Int>()
    val currentPosition: LiveData<Int>
        get() = _currentPosition

    private val _res = MutableLiveData<Resource<QuestionList>>()

    val res : LiveData<Resource<QuestionList>>
        get() = _res

    //Given list of questions
    fun getSolutionsList(nQuestion: String, catID: String, diffType: String, queType: String) {
        viewModelScope.launch {
            _solutionRes.value =
                solutionsUseCase.solutionsExecute(nQuestion, catID, diffType, queType)
        }
    }

    // Set the initial position to 0
    init {
        _currentPosition.value = 0
    }

    // Increment the position by 1 and update the LiveData
    fun onButtonClicked() {
        _currentPosition.value = (_currentPosition.value ?: 0) + 1
    }

}