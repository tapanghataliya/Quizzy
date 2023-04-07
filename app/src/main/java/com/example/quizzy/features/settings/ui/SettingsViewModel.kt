package com.example.quizzy.features.settings.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.quizzy.core.base.BaseNavigator
import com.example.quizzy.core.base.BaseViewModel
import com.example.quizzy.core.utils.Constant.Companion.CATEGORY_ID
import com.example.quizzy.core.utils.Constant.Companion.DIFFICULTY_TYPE
import com.example.quizzy.core.utils.Constant.Companion.ISCHECKED
import com.example.quizzy.core.utils.Constant.Companion.NUMBER_QUESTION
import com.example.quizzy.core.utils.Constant.Companion.QUESTIONS_TYPE
import com.example.quizzy.core.utils.NetworkUtils
import com.example.quizzy.core.utils.Resource
import com.example.quizzy.features.settings.data.CategoryListResponse
import com.example.quizzy.features.settings.data.TriviaCategory
import com.example.quizzy.features.settings.domain.SettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingRepository: SettingRepository,
    private val sharedPreferences: SharedPreferences
) : BaseViewModel<BaseNavigator>() {

    val difficultyList = MutableLiveData<List<TriviaCategory>>()
    val questionTypeList = MutableLiveData<List<TriviaCategory>>()
    val numberQuesList = MutableLiveData<List<String>>()

    private val _categoryRes = MutableLiveData<Resource<CategoryListResponse>>()
    val categorysResponse: LiveData<Resource<CategoryListResponse>>
        get() = _categoryRes

    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected: LiveData<Boolean>
        get() = _isConnected

    //Get Category list from server
    fun getCategory() =
        viewModelScope.launch {
            _categoryRes.postValue(Resource.loading(null))
            settingRepository.getCategoryList().let {
                if (it.isSuccessful){
                    _categoryRes.postValue(Resource.success(it.body()))
                }else{
                    _categoryRes.postValue(Resource.error(it.errorBody().toString(),null))
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
            .putString(NUMBER_QUESTION, nQuestion)
            .putString(CATEGORY_ID, categoryID)
            .putString(DIFFICULTY_TYPE, difficultyType)
            .putString(QUESTIONS_TYPE, questionsType)
            .putBoolean(ISCHECKED, isCheck)
            .apply()
    }

    //Get the data from shared preference
    fun getNumberQuestions() = sharedPreferences.getString(NUMBER_QUESTION, null)
    fun getCategorysID() = sharedPreferences.getString(CATEGORY_ID, null)
    fun getDifficultysType() = sharedPreferences.getString(DIFFICULTY_TYPE, null)
    fun getQuestionssType() = sharedPreferences.getString(QUESTIONS_TYPE, null)
    fun getIsChecked() = sharedPreferences.getBoolean(ISCHECKED,false)

    //Check Internet connection
    @RequiresApi(Build.VERSION_CODES.M)
    fun checkInternetConnection(context: Context) {
        _isConnected.value = NetworkUtils.isNetworkConnected(context)
    }
}