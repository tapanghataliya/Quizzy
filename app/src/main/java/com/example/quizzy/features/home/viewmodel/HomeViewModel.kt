package com.example.quizzy.features.home.viewmodel

import com.example.quizzy.core.base.BaseNavigator
import com.example.quizzy.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
): BaseViewModel<BaseNavigator>() {
}