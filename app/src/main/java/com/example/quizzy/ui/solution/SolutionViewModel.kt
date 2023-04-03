package com.example.quizzy.ui.solution

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.quizzy.base.BaseNavigator
import com.example.quizzy.base.BaseViewModel
import com.example.quizzy.data.quiz.QuestionList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SolutionViewModel@Inject constructor(
    private val solutionRepository: SolutionRepository
) : BaseViewModel<BaseNavigator>()  {

    val solutionList = MutableLiveData<QuestionList>()
    private var solutionResponse: QuestionList? = null

    private val _currentPosition = MutableLiveData<Int>()
    val currentPosition: LiveData<Int>
        get() = _currentPosition

    //Given list of questions
    fun getQuestionList(nQuestion: String, catID: String, diffType: String, queType: String) =
        viewModelScope.launch(Dispatchers.IO) {

            solutionRepository.getQuestionList(nQuestion, catID, diffType, queType).collect {
                solutionResponse = it.data
                solutionResponse?.let { quize ->
                    solutionList.postValue(quize)
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