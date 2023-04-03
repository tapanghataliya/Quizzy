package com.example.quizzy.ui.quiz

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.quizzy.R
import com.example.quizzy.base.BaseNavigator
import com.example.quizzy.base.BaseViewModel
import com.example.quizzy.data.quiz.QuestionList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizeViewModel @Inject constructor(
    private val quizRepository: QuizRepository
) : BaseViewModel<BaseNavigator>() {

    val quizList = MutableLiveData<QuestionList>()
    private var quizResponse: QuestionList? = null

    private val _remainingTime = MutableLiveData<Long>()
    val remainingTime: LiveData<Long>
        get() = _remainingTime

    private val _currentPosition = MutableLiveData<Int>()
    val currentPosition: LiveData<Int>
        get() = _currentPosition

    private var countDownTimer: CountDownTimer? = null

    private val _incorrectAnswerSound = MutableLiveData<Int>()
    val incorrectAnswerSound: LiveData<Int>
        get() = _incorrectAnswerSound

    val vibrationDurations = MutableLiveData<Long>()
    val vibrationDuration: LiveData<Long>
        get() = vibrationDurations


    //Given list of questions
    fun getQuestionList(nQuestion: String, catID: String, diffType: String, queType: String) =
        viewModelScope.launch(Dispatchers.IO) {

            quizRepository.getQuestionList(nQuestion, catID, diffType, queType).collect {
                quizResponse = it.data
                quizResponse?.let { quize ->
                    quizList.postValue(quize)
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

    //Count timer for 30 seconds
    fun startTimer(duration: Long) {
        countDownTimer = object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _remainingTime.value = millisUntilFinished / 1000
            }

            override fun onFinish() {
                _remainingTime.value = 0
            }
        }
        countDownTimer?.start()
    }

    //Cancel timer
    fun cancelTimer() {
        countDownTimer?.cancel()
    }

    //Incorrect answer sound
    fun onIncorrectAnswerSelected() {
        _incorrectAnswerSound.value = R.raw.incorrect_sound
    }
}