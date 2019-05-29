package com.example.ownrepositarypatternsample.ui.login

import com.example.ownrepositarypatternsample.R
import com.example.ownrepositarypatternsample.base.BaseActivity
import com.example.ownrepositarypatternsample.base.repository.ScreenState
import com.example.ownrepositarypatternsample.data.remote.pojo.LoginPojo
import com.example.ownrepositarypatternsample.databinding.ActivityLoginBinding
import com.example.ownrepositarypatternsample.ui.main.MainActivity
import com.kotlinlibrary.utils.ktx.observeLiveData
import com.kotlinlibrary.utils.navigate.launchActivity

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(R.layout.activity_login) {
    override fun initObserve() {
        super.initObserve()
        observeLiveData(mViewModel.getScreenStateObservable()) { screenState(it) }
    }

    private fun screenState(resource: ScreenState<LoginPojo>) {
        when(resource) {
            is ScreenState.ErrorState.Validation -> {
                mViewModel.message.postValue(getString(resource.message))
            }
            is ScreenState.SuccessState.Validation -> {
                resource.request?.let { bean ->
                    launchActivity<MainActivity>(finishCurrent = true, params = *arrayOf("login" to bean))
                }
            }
        }
    }
}