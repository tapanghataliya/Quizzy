package com.example.quizzy.features.settings.ui

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.quizzy.BR
import com.example.quizzy.R
import com.example.quizzy.core.base.BaseFragment
import com.example.quizzy.core.utils.Constant.Companion.showSnackBar
import com.example.quizzy.core.utils.Status
import com.example.quizzy.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 * A simple Setting Fragment.
 * In this fragment display category, difficulty, type, sound enable and vibration enable in this screen.
 */
@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding, SettingsViewModel>() {

    lateinit var numberQuestions : String
    lateinit var categoryID : String
    lateinit var difficultyType : String
    lateinit var questionsType : String
    private var isCheck = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getNumberOfQueList()
        getCategortList()
        getDifficultyList()
        getQuestionTypeList()
        clickHandle()
    }

    //Number of questions list
    private fun getNumberOfQueList() {
        viewModel.getNumberOfQuestion()
        viewModel.numberQuesList.observe(viewLifecycleOwner){ data ->
            val difficultyAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                data
            )

            getBindingClass().spnNumberQuestions.adapter = difficultyAdapter

            //Item click to get number of questions
            getBindingClass().spnNumberQuestions.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

                    numberQuestions = parent.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }
    }

    //Category List display.
    private fun getCategortList() {

        viewModel.categorysResponse.observe(viewLifecycleOwner, Observer {
            when(it.status){
                Status.LOADING->{
                    getBindingClass().progressBar.visibility = View.VISIBLE
                }

                Status.SUCCESS->{
                    getBindingClass().progressBar.visibility = View.GONE

                    it.data.let{res->

                        val spinnerAdapter =
                            ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                res!!.trivia_categories.map { it1 ->
                                    it1.name
                                }
                            )
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        getBindingClass().spnCategory.adapter = spinnerAdapter

                        //Item click to get category ID
                        getBindingClass().spnCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

                                categoryID = viewModel.categorysResponse.value?.data?.trivia_categories?.get(position)?.id.toString()
                            }

                            override fun onNothingSelected(parent: AdapterView<*>) {
                            }
                        }
                    }
                }

                Status.ERROR->{
                    getBindingClass().progressBar.visibility = View.GONE
                    view?.showSnackBar(it.message)
                }
            }
        })
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
        }
        viewModel.getQuestionType()
    }

    private fun clickHandle() {

        getBindingClass().switchSound.setOnCheckedChangeListener{_, isChecked ->
            isCheck = isChecked
            viewModel.getIsChecked()
        }

        getBindingClass().txtSubmit.setOnClickListener {
            // Saved string in SharedPreferences
            viewModel.saveSettings(numberQuestions,categoryID, difficultyType, questionsType, isCheck)
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

}