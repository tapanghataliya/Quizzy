package com.example.quizzy.core.di

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import com.example.quizzy.core.apiservices.ApiService
import com.example.quizzy.core.utils.Constant.Companion.BaseURL
import com.example.quizzy.core.utils.Constant.Companion.KEY_USER_PREFERENCES
import com.example.quizzy.core.utils.MyPreferencesManager
import com.example.quizzy.features.question.domain.usecase.QuestionsUseCase
import com.example.quizzy.features.question.presentation.viewmodel.QuestionsViewModel
import com.example.quizzy.features.settings.domain.usecase.SettingsUseCase
import com.example.quizzy.features.settings.presentation.viewmodel.SettingViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BaseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiClient(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

//    @Provides
//    @Singleton
//    fun provideApiHelper(apiHelper: ApiHelperImpl): ApiHelper = apiHelper

    @Provides
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(KEY_USER_PREFERENCES, Context.MODE_PRIVATE)
    }

    @Provides
    fun provideSettingViewModel(
        settingsUseCase: SettingsUseCase,
        sharedPreferenceData: MyPreferencesManager
    ): SettingViewModel {
        return SettingViewModel(settingsUseCase, sharedPreferenceData)
    }

    @Provides
    fun provideQuestionsViewModel(
        questionsUseCase: QuestionsUseCase
    ):QuestionsViewModel{
        return  QuestionsViewModel(questionsUseCase)
    }
}
