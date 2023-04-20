package com.example.quizzy.features.settings.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.quizzy.core.base.BaseNavigator
import com.example.quizzy.core.base.BaseViewModel
import com.example.quizzy.core.utils.MyPreferencesManager
import com.example.quizzy.core.utils.Resource
import com.example.quizzy.features.settings.data.model.MySettingsData
import com.example.quizzy.features.settings.data.model.TriviaCategory
import com.example.quizzy.features.settings.domain.usecase.SettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel@Inject constructor(
    private val settingsUseCase: SettingsUseCase,
    private val sharedPreferenceData: MyPreferencesManager
) : BaseViewModel<BaseNavigator>() {

    private val _categoryRes = MutableLiveData<Resource<List<TriviaCategory>>>()
    val categorysResponse: LiveData<Resource<List<TriviaCategory>>> = _categoryRes

    val difficultyList = MutableLiveData<List<TriviaCategory>>()
    val questionTypeList = MutableLiveData<List<TriviaCategory>>()
    val numberQuesList = MutableLiveData<List<String>>()

    //Get Category list from server
    fun getCategory() {
        viewModelScope.launch {
            _categoryRes.value = settingsUseCase.categoryExecute()
        }
    }
    //Get Question type from static
    fun getNumberOfQuestion() {
        val data = mutableListOf<String>()
        for (i in 1..20) {
            data.add(i.toString())
        }
        numberQuesList.value = data
    }
    //Get Question difficulty type from static
    fun getDifficultyList() {
        val difficulty = listOf(
            TriviaCategory(1, "easy"),
            TriviaCategory(2, "medium"),
            TriviaCategory(3, "hard")
        )
        difficultyList.value = difficulty
    }

    //Get Question type from static
    fun getQuestionType() {
        val type = listOf(
            TriviaCategory(1, "multiple"),
            TriviaCategory(2, "True/False")
        )
        questionTypeList.value = type
    }

    //Save the data in shared preference
    fun saveSettingData(mySettingsData: MySettingsData) {
        viewModelScope.launch(Dispatchers.IO){
            sharedPreferenceData.saveSettingData(mySettingsData)
        }
    }

    //Get the data from shared preference
    fun getsaveSettingData(): MySettingsData {
        return sharedPreferenceData.getsaveSettingData()
    }

}