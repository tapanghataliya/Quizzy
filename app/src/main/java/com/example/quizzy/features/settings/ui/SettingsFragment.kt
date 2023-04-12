package com.example.quizzy.features.settings.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.quizzy.BR
import com.example.quizzy.R
import com.example.quizzy.core.base.BaseFragment
import com.example.quizzy.core.utils.Constant.Companion.NO_INTERNET
import com.example.quizzy.core.utils.Constant.Companion.NO_RECORD
import com.example.quizzy.core.utils.Constant.Companion.showSnackBar
import com.example.quizzy.core.utils.Constant.Companion.showSnackRedBar
import com.example.quizzy.core.utils.Status
import com.example.quizzy.databinding.FragmentSettingsBinding
import com.example.quizzy.features.settings.data.MySettingsData
import dagger.hilt.android.AndroidEntryPoint


/**
 * A simple Setting Fragment.
 * In this fragment display category, difficulty, type, sound enable and vibration enable in this screen.
 */
@Suppress("DEPRECATION")
@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding, SettingsViewModel>() {

    lateinit var numberQuestions : String
    lateinit var categoryID : String
    lateinit var categoryName : String
    lateinit var difficultyType : String
    lateinit var questionsType : String
    private var isCheck = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let { viewModel.checkInternetConnection(it) }
        viewModel.isConnected.observe(viewLifecycleOwner) { isConnected->
            if (isConnected){
                getBindingClass().rlMain.visibility = View.VISIBLE
                getBindingClass().imgNoInternet.visibility = View.GONE
                getCategortList()
            }else{
                view.showSnackRedBar(NO_INTERNET)
                getBindingClass().rlMain.visibility = View.GONE
                getBindingClass().imgNoInternet.visibility = View.VISIBLE
            }
            getNumberOfQueList()
            getDifficultyList()
            getQuestionTypeList()
            buttonClickHandler()
        }
    }

    //Number of questions list
    private fun getNumberOfQueList() {
        viewModel.getNumberOfQuestion()
        viewModel.numberQuesList.observe(viewLifecycleOwner){ data ->
            val numberAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                data
            )

            getBindingClass().spnNumberQuestions.adapter = numberAdapter

            //Item click to get number of questions
            getBindingClass().spnNumberQuestions.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

                    numberQuestions = parent.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
            val spinnerPosition = numberAdapter.getPosition(viewModel.getsaveSettingData().nQuestion)
            getBindingClass().spnNumberQuestions.setSelection(spinnerPosition)
        }
    }

    //Category List display.
    private fun getCategortList() {

        viewModel.categorysResponse.observe(viewLifecycleOwner) {
            when(it.status){
                Status.LOADING->{
                    getBindingClass().progressBar.visibility = View.VISIBLE
                }

                Status.SUCCESS->{
                    getBindingClass().progressBar.visibility = View.GONE

                    it.data.let{res->

                        val categorysAdapter =
                            ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                res!!.trivia_categories.map { it1 ->
                                    it1.name
                                }
                            )
                        categorysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        getBindingClass().spnCategory.adapter = categorysAdapter

                        //Item click to get category ID
                        getBindingClass().spnCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

                                categoryID = viewModel.categorysResponse.value?.data?.trivia_categories?.get(position)?.id.toString()
                                categoryName = viewModel.categorysResponse.value?.data?.trivia_categories?.get(position)?.name.toString()
                            }

                            override fun onNothingSelected(parent: AdapterView<*>) {
                            }
                        }
                        val spinnerPosition = categorysAdapter.getPosition(viewModel.getsaveSettingData().categoryName)
                        getBindingClass().spnCategory.setSelection(spinnerPosition)
                    }
                }

                Status.ERROR->{
                    getBindingClass().progressBar.visibility = View.GONE
                    view?.showSnackBar(NO_RECORD)
                }
            }
        }
        viewModel.getCategory()
    }

    //Display Difficulty list
    private fun getDifficultyList() {

        viewModel.difficultyList.observe(viewLifecycleOwner){ difficultyList ->
            val difficultyAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                difficultyList.map {
                    it.name
                }
            )
            getBindingClass().spnDifficulty.adapter = difficultyAdapter

            //Item click to get difficulty Type
            getBindingClass().spnDifficulty.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

                    difficultyType = parent.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
            val spinnerPosition = difficultyAdapter.getPosition(viewModel.getsaveSettingData().difficultyType)
            getBindingClass().spnDifficulty.setSelection(spinnerPosition)
        }
        viewModel.getDifficultyList()
    }

    //Display Question type
    private fun getQuestionTypeList() {
        viewModel.questionTypeList.observe(viewLifecycleOwner){questionType ->
            val typeAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                questionType.map {
                    it.name
                }
            )
            getBindingClass().spnType.adapter = typeAdapter
            //Item click to get questions Type
            getBindingClass().spnType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    questionsType = parent.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Do nothing when nothing is selected
                }
            }
            val spinnerPosition = typeAdapter.getPosition(viewModel.getsaveSettingData().questionsType)
            getBindingClass().spnType.setSelection(spinnerPosition)
        }
        viewModel.getQuestionType()
    }

    private fun buttonClickHandler() {

        getBindingClass().switchSound.isChecked = viewModel.getsaveSettingData().isCheck != false
        getBindingClass().switchSound.setOnCheckedChangeListener{_, isChecked ->
            isCheck = isChecked
            viewModel.getsaveSettingData().isCheck
        }

        getBindingClass().txtSubmit.setOnClickListener {
            // Saved data in SharedPreferences
            val mySettingsData = MySettingsData(numberQuestions,categoryID,categoryName, difficultyType, questionsType, isCheck)
            viewModel.saveSettingData(mySettingsData)
            activity?.onBackPressed()
        }
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getBindingClass(): FragmentSettingsBinding {
        return (getViewDataBinding() as FragmentSettingsBinding)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_settings
    }

    override fun onDestroy() {
        super.onDestroy()
        getBindingClass().unbind()
    }
}