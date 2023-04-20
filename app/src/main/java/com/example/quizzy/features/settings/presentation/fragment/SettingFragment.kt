package com.example.quizzy.features.settings.presentation.fragment

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.example.quizzy.BR
import com.example.quizzy.R
import com.example.quizzy.core.base.BaseFragment
import com.example.quizzy.core.utils.Constant
import com.example.quizzy.core.utils.Status
import com.example.quizzy.core.utils.ViewExt.Companion.showSnackBar
import com.example.quizzy.databinding.FragmentSettingsBinding
import com.example.quizzy.features.settings.data.model.MySettingsData
import com.example.quizzy.features.settings.presentation.viewmodel.SettingViewModel
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingsBinding, SettingViewModel>() {

    lateinit var numberQuestions : String
    lateinit var categoryID : String
    lateinit var categoryName : String
    lateinit var difficultyType : String
    lateinit var questionsType : String
    private var isCheck = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[SettingViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getCategoryList()
        getNumberOfQueList()
        getDifficultyList()
        getQuestionTypeList()
        buttonClickHandler()
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
    private fun getCategoryList() {
        viewModel.categorysResponse.observe(viewLifecycleOwner){
            when(it.status){
                Status.LOADING->{
                    getBindingClass().progressBar.visibility = View.VISIBLE
                }

                Status.SUCCESS->{
                    it.data.let { res->
                        val categoryAdapter =
                            ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                res!!.map { it1 ->
                                    it1.name
                                }
                            )

                        getBindingClass().progressBar.visibility =View.GONE
                        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        getBindingClass().spnCategory.adapter = categoryAdapter

                        //Item click to get category ID
                        getBindingClass().spnCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

                                categoryID = viewModel.categorysResponse.value?.data?.get(position)?.id.toString()
                                categoryName = viewModel.categorysResponse.value?.data?.get(position)?.name.toString()

                                val spinnerPosition = categoryAdapter.getPosition(viewModel.getsaveSettingData().categoryName)
                                getBindingClass().spnCategory.setSelection(spinnerPosition)
                            }

                            override fun onNothingSelected(parent: AdapterView<*>) {
                            }
                        }
                    }
                }

                Status.ERROR->{
                    getBindingClass().progressBar.visibility = View.GONE
                    view?.showSnackBar(Constant.NO_INTERNET)
                    getBindingClass().rlMain.visibility = View.GONE
                    getBindingClass().imgNoInternet.visibility = View.VISIBLE
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

    //Click handle
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