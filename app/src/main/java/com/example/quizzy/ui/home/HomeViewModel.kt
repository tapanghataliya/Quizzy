package com.example.quizzy.ui.home

import com.example.quizzy.base.BaseNavigator
import com.example.quizzy.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
): BaseViewModel<BaseNavigator>() {
}