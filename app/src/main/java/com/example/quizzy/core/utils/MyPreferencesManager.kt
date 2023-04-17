package com.example.quizzy.core.utils

import android.content.SharedPreferences
import com.example.quizzy.features.settings.data.MySettingsData
import javax.inject.Inject

class MySettingSharedPreference @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    //Save the data in shared preference
    fun saveSettingData(mySettingsData: MySettingsData) {
        sharedPreferences.edit()
            .putString(Constant.NUMBER_QUESTION, mySettingsData.nQuestion)
            .putString(Constant.CATEGORY_ID, mySettingsData.categoryID)
            .putString(Constant.CATEGORY_NAME, mySettingsData.categoryName)
            .putString(Constant.DIFFICULTY_TYPE, mySettingsData.difficultyType)
            .putString(Constant.QUESTIONS_TYPE, mySettingsData.questionsType)
            .putBoolean(Constant.ISCHECKED, mySettingsData.isCheck)
            .apply()
    }

    //Get the data from shared preference
    fun getsaveSettingData(): MySettingsData {
        val numberQuestions = sharedPreferences.getString(Constant.NUMBER_QUESTION, "") ?: ""
        val categorysID = sharedPreferences.getString(Constant.CATEGORY_ID, "") ?: ""
        val categoryName = sharedPreferences.getString(Constant.CATEGORY_NAME, "") ?: ""
        val difficultysType = sharedPreferences.getString(Constant.DIFFICULTY_TYPE, "") ?: ""
        val questionssType = sharedPreferences.getString(Constant.QUESTIONS_TYPE, "") ?: ""
        val IsChecked = sharedPreferences.getBoolean(Constant.ISCHECKED, false)

        return MySettingsData(
            numberQuestions,
            categorysID,
            categoryName,
            difficultysType,
            questionssType,
            IsChecked
        )
    }
}