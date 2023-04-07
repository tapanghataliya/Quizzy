package com.example.quizzy.features.solution.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.quizzy.core.base.BaseNavigator
import com.example.quizzy.core.base.BaseViewModel
import com.example.quizzy.core.utils.Resource
import com.example.quizzy.features.question.data.QuestionList
import com.example.quizzy.features.solution.domain.SolutionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SolutionViewModel @Inject constructor(
    private val solutionRepository: SolutionRepository
) : BaseViewModel<BaseNavigator>() {

    private val _solutionRes = MutableLiveData<Resource<QuestionList>>()
    val solutionResponse: LiveData<Resource<QuestionList>>
        get() = _solutionRes

    private val _currentPosition = MutableLiveData<Int>()
    val currentPosition: LiveData<Int>
        get() = _currentPosition

    private val _res = MutableLiveData<Resource<QuestionList>>()

    val res : LiveData<Resource<QuestionList>>
        get() = _res

    //Given list of questions
    fun getQuestionList(nQuestion: String, catID: String, diffType: String, queType: String) =
        viewModelScope.launch {
            _solutionRes.postValue(Resource.loading(null))
            solutionRepository.getSolutionList(nQuestion, catID, diffType, queType).let {
                if (it.isSuccessful) {
                    _solutionRes.postValue(Resource.success(it.body()))
                } else {
                    _solutionRes.postValue(Resource.error(it.errorBody().toString(), null))
                }
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