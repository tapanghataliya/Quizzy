package com.example.quizzy.ui.splash

import android.content.Intent
import android.os.Bundle
import com.example.quizzy.R
import com.example.quizzy.base.BaseActivity
import com.example.quizzy.navigators.SplashNavigator
import com.example.quizzy.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

/**
 * A simple Splash screen.
 * To display 2 second after redirect home screen.
 */
@AndroidEntryPoint
class SplashActivity: BaseActivity(), SplashNavigator {

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        GlobalScope.launch(Dispatchers.Main){
            delay(2000)
            navigateToDashboard()
        }
    }

    //Navigate to home screen
    override fun navigateToDashboard() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}