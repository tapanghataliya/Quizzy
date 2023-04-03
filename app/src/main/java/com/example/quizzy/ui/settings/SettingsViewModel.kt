package com.example.quizzy.ui.settings

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.quizzy.base.BaseNavigator
import com.example.quizzy.base.BaseViewModel
import com.example.quizzy.data.Category.CategoryListResponse
import com.example.quizzy.data.Category.TriviaCategory
import com.example.quizzy.utils.Constant.Companion.categorysId
import com.example.quizzy.utils.Constant.Companion.difficultySType
import com.example.quizzy.utils.Constant.Companion.isChecked
import com.example.quizzy.utils.Constant.Companion.numberOfQue
import com.example.quizzy.utils.Constant.Companion.questionSType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingRepository: SettingRepository,
    private val sharedPreferences: SharedPreferences
) : BaseViewModel<BaseNavigator>() {

    val categoryList = MutableLiveData<CategoryListResponse>()
    private var categoryResponse: CategoryListResponse? = null

    val difficultyList = MutableLiveData<List<TriviaCategory>>()
    val questionTypeList = MutableLiveData<List<TriviaCategory>>()
    val numberQuesList = MutableLiveData<List<String>>()

    //Get Category list from server
    fun getCetegory() =
        viewModelScope.launch(Dispatchers.IO) {

            settingRepository.getCetegory().collect {
                categoryResponse = it.data
                categoryResponse?.let { category ->
                    categoryList.postValue(category)
                }
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
    fun saveSettings(nQuestion:String, categoryID: String, difficultyType: String, questionsType: String, isCheck: Boolean) {
        sharedPreferences.edit()
            .putString(numberOfQue, nQuestion)
            .putString(categorysId, categoryID)
            .putString(difficultySType, difficultyType)
            .putString(questionSType, questionsType)
            .putBoolean(isChecked, isCheck)
            .apply()
    }

    //Get the data from shared preference
    fun getNumberQuestions() = sharedPreferences.getString(numberOfQue, null)
    fun getCategorysID() = sharedPreferences.getString(categorysId, null)
    fun getDifficultysType() = sharedPreferences.getString(difficultySType, null)
    fun getQuestionssType() = sharedPreferences.getString(questionSType, null)
    fun getIsChecked() = sharedPreferences.getBoolean(isChecked,true)

}