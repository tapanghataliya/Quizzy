package com.example.quizzy.ui.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.quizzy.BR
import com.example.quizzy.R
import com.example.quizzy.base.BaseFragment
import com.example.quizzy.databinding.FragmentHomeBinding
import com.example.quizzy.navigators.SettingNavigator
import com.example.quizzy.utils.Constant.Companion.SETTIMER
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple HomeFragment.
 * To display option which type of test are available and setting also.
 */
@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(),SettingNavigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickHandle()
    }

    //Handle the click for redirection
    private fun clickHandle() {
        getBindingClass().startPlay.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(SETTIMER, "Timer")
            findNavController().navigate(R.id.quizeFragment2, bundle)
        }

        getBindingClass().normalPlay.setOnClickListener {
            findNavController().navigate(R.id.quizeFragment2)
        }

        getBindingClass().settings.setOnClickListener {
            navigateToSetting()
        }
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getBindingClass(): FragmentHomeBinding {
        return (getViewDataBinding() as FragmentHomeBinding)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun navigateToSetting() {
        findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
    }

}