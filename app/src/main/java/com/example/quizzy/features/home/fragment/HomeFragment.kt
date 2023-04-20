package com.example.quizzy.features.home.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.quizzy.BR
import com.example.quizzy.R
import com.example.quizzy.core.base.BaseFragment
import com.example.quizzy.core.utils.Constant.Companion.SET_TIMER
import com.example.quizzy.databinding.FragmentHomeBinding
import com.example.quizzy.features.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple HomeFragment.
 * To display option which type of test are available and setting also.
 */
@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonClickHandle()
    }

    //Handle the click for redirection
    private fun buttonClickHandle() {
        getBindingClass().startPlay.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(SET_TIMER, "Timer")
            findNavController().navigate(R.id.quizeFragment2, bundle)
        }

        getBindingClass().normalPlay.setOnClickListener {
            findNavController().navigate(R.id.quizeFragment2)
        }

        getBindingClass().settings.setOnClickListener {
            findNavController().navigate(R.id.settingsFragment)
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

    override fun onDestroy() {
        super.onDestroy()
        getBindingClass().unbind()
    }
}