package com.example.quizzy.features.splash.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.example.quizzy.R
import com.example.quizzy.core.base.BaseActivity
import com.example.quizzy.core.navigators.SplashNavigator
import com.example.quizzy.features.MainActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple Splash screen.
 * To display 2 second after redirect home screen.
 */
@AndroidEntryPoint
class SplashActivity : BaseActivity(), SplashNavigator {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        viewModel.splashCompleted.observe(this) {
            if (it) {
                navigateToDashboard()
            }
        }
        viewModel.splashRedirect()

    }

    //Navigate to home screen
    override fun navigateToDashboard() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}