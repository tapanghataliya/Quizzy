package com.example.quizzy.features.quiz.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.quizzy.R
import com.example.quizzy.core.base.BaseNavigator
import com.example.quizzy.core.base.BaseViewModel
import com.example.quizzy.core.utils.Resource
import com.example.quizzy.core.utils.VibrationUtils
import com.example.quizzy.features.quiz.data.QuestionList
import com.example.quizzy.features.quiz.domain.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val questionRepository: QuestionRepository
) : BaseViewModel<BaseNavigator>() {

    private val _questionRes = MutableLiveData<Resource<QuestionList>>()
    val questionResponse: LiveData<Resource<QuestionList>>
        get() = _questionRes

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

    //Given list of questions
    fun getQuestionList(nQuestion: String, catID: String, diffType: String, queType: String) =
        viewModelScope.launch {
            _questionRes.postValue(Resource.loading(null))
            questionRepository.getQuestionList(nQuestion, catID, diffType, queType).let {
                if (it.isSuccessful) {
                    _questionRes.postValue(Resource.success(it.body()))
                } else {
                    _questionRes.postValue(Resource.error(it.errorBody().toString(), null))
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

    //Incorrect answer sound
    fun onIncorrectAnswerSelected() {
        _incorrectAnswerSound.value = R.raw.incorrect_sound
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

    fun onClickVibrat(context: Context) {
        VibrationUtils.vibrate(context)
    }
}