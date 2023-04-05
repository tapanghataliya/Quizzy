package com.example.quizzy.features.splash.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizzy.core.utils.Constant.Companion.SPLASH_SCREEN_TIMEOUT
import kotlinx.coroutines.*

class SplashViewModel:ViewModel() {

    private val _splashCompleted = MutableLiveData<Boolean>()
    val splashCompleted:LiveData<Boolean>
    get() = _splashCompleted

    @OptIn(DelicateCoroutinesApi::class)
    fun splashRedirect(){
        GlobalScope.launch(Dispatchers.Main){
            _splashCompleted.value = true
            delay(SPLASH_SCREEN_TIMEOUT)
        }
    }
}