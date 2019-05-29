package com.example.ownrepositarypatternsample.ui.login

import androidx.lifecycle.MutableLiveData
import com.example.ownrepositarypatternsample.R
import com.example.ownrepositarypatternsample.base.BaseViewModel
import com.example.ownrepositarypatternsample.base.repository.ScreenState
import com.example.ownrepositarypatternsample.data.remote.pojo.LoginPojo
import com.kotlinlibrary.validation.bindValidator

class LoginViewModel: BaseViewModel() {
    private var screenStateLiveData: MutableLiveData<ScreenState<LoginPojo>> = MutableLiveData()

    val voFirstName = bindValidator<Int>("", false)
        .nonEmpty(R.string.vld_first_name_empty)
        .minLength(3, R.string.vld_first_name_more_than_3)
        .addErrorCallback {
            it?.let { error ->
                screenStateLiveData.value = ScreenState.ErrorState.Validation(error)
            }
        }

    val voLastName = bindValidator<Int>("", false)
        .nonEmpty(R.string.vld_last_name_empty)
        .minLength(3, R.string.vld_last_name_more_than_3)
        .addErrorCallback {
            it?.let { error ->
                screenStateLiveData.value = ScreenState.ErrorState.Validation(error)
            }
        }

    val voEmail = bindValidator<Int>("", false)
        .nonEmpty(R.string.vld_email_address_empty)
        .validEmail(R.string.vld_invalid_email_address)
        .addErrorCallback {
            it?.let { error ->
                screenStateLiveData.value = ScreenState.ErrorState.Validation(error)
            }
        }

    val voPassword = bindValidator<Int>("", false)
        .nonEmpty(R.string.vld_password_empty)
        .regex(".*[A-Z]+.*", R.string.vld_password_contain_capital_letter)
        .regex(".*[0-9]+.*", R.string.vld_password_contain_digits)
        .regex(".*[a-z]+.*", R.string.vld_password_contain_small_letter)
        .minLength(8, R.string.vld_password_more_than_eight)
        .maxLength(16, R.string.vld_password_less_than_sixteen)
        .addErrorCallback {
            it?.let { error ->
                screenStateLiveData.value = ScreenState.ErrorState.Validation(error)
            }
        }

    fun onNext() {
        if(voFirstName.check() && voLastName.check() && voEmail.check() && voPassword.check()) {
            screenStateLiveData.value = ScreenState.SuccessState.Validation(
                LoginPojo(voFirstName.getValue(), voLastName.getValue(), voEmail.getValue(), voPassword.getValue())
            )
        }
    }

    fun getScreenStateObservable() = screenStateLiveData
}