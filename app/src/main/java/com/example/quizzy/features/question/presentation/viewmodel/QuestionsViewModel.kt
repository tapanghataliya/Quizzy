package com.example.quizzy.features.question.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.quizzy.R
import com.example.quizzy.core.base.BaseNavigator
import com.example.quizzy.core.base.BaseViewModel
import com.example.quizzy.core.utils.Resource
import com.example.quizzy.core.utils.VibrationUtils
import com.example.quizzy.features.question.domain.model.QuestionslistDomain
import com.example.quizzy.features.question.domain.usecase.QuestionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class QuestionsViewModel @Inject constructor(
    private val questionsUseCase: QuestionsUseCase
) : BaseViewModel<BaseNavigator>() {

    private val _questionRes = MutableLiveData<Resource<QuestionslistDomain>>()
    val questionResponse: LiveData<Resource<QuestionslistDomain>> = _questionRes

    private val _currentTime = MutableLiveData<String>()
    val currentTime: LiveData<String>
        get() = _currentTime

    private var timer: Timer? = null
    private var remainingTimes = 0

    private val _currentPosition = MutableLiveData<Int>()
    val currentPosition: LiveData<Int>
        get() = _currentPosition

    private val _incorrectAnswerSound = MutableLiveData<Int>()
    val incorrectAnswerSound: LiveData<Int>
        get() = _incorrectAnswerSound

    private val _correctAnswerSound = MutableLiveData<Int>()
    val correctAnswerSound: LiveData<Int>
        get() = _correctAnswerSound

    private val _currentPage = MutableLiveData<Int>()
    val currentPage: LiveData<Int>
        get() = _currentPage

    //Given list of questions
    fun getQuestionsList(nQuestion: String, catID: String, diffType: String, queType: String) {
        viewModelScope.launch {
            _questionRes.value =
                questionsUseCase.questionsExecute(nQuestion, catID, diffType, queType)
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

    //Incorrect answer sound
    fun onIncorrectAnswerSelected() {
        _incorrectAnswerSound.value = R.raw.incorrect_sound
    }

    //Correct answer sound play
    fun onCorrectAnswerSelected() {
        _correctAnswerSound.value = R.raw.currect_sound
    }

    //start timer
    fun startTimer() {
        timer?.cancel()
        timer = Timer()

        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                remainingTimes--
                val minutes = remainingTimes / 60
                val seconds = remainingTimes % 60
                _currentTime.postValue("%02d:%02d".format(minutes, seconds))
                if (remainingTimes <= 0) {
                    timer?.cancel()
                }
            }
        }, 0, 1000)

        remainingTimes = 20 * 60
    }

    fun cancelTimer() {
        timer?.cancel()
    }

    //vibrate click event
    fun onClickVibrat(context: Context) {
        VibrationUtils.vibrate(context)
    }

    //Next question move position wise
    fun nextQuestion() {
        _currentPage.value = (_currentPage.value ?: 0) + 1
    }
}