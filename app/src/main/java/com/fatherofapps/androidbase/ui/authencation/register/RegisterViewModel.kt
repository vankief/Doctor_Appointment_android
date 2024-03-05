package com.fatherofapps.androidbase.ui.authencation.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fatherofapps.androidbase.base.viewmodel.BaseViewModel
import com.fatherofapps.androidbase.common.Constants.ACCESS_TOKEN
import com.fatherofapps.androidbase.common.Constants.IS_FIRST_TIME
import com.fatherofapps.androidbase.common.Constants.IS_LOGGED_IN
import com.fatherofapps.androidbase.common.Constants.REFRESH_TOKEN
import com.fatherofapps.androidbase.data.repositories.AuthRepository
import com.fatherofapps.androidbase.data.request.SignUpRequest
import com.fatherofapps.androidbase.data.response.ConfigResponse
import com.fatherofapps.androidbase.data.response.SignUpResponse
import com.fatherofapps.androidbase.helper.preferences.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferenceManager: PreferenceManager
) : BaseViewModel() {
    private var _registerResponse = MutableLiveData<ConfigResponse<SignUpResponse>>()

    val registerResponse: MutableLiveData<ConfigResponse<SignUpResponse>>
        get() = _registerResponse
    fun doSignUp(req: SignUpRequest) {
        showLoading(true)
        parentJob = viewModelScope.launch(handler) {
            val data = authRepository.doSignUp(req)
            _registerResponse.postValue(data!!)
        }
        registerJobFinish()
    }
    fun validateCredentials(
        emailAddress: String, password: String, confirmPassword: String
    ): Pair<Boolean, String> {
        var result = Pair(true, "")
        if (emailAddress.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            result = Pair(false, "Please provide the credentials")
        } else if (!emailAddress.isEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            result = Pair(false, "Email is invalid")
        } else if (!password.isEmpty() && password.length <= 6) {
            result = Pair(false, "Password length should be greater than 6")
        } else if (!confirmPassword.isEmpty() && confirmPassword.length <= 6) {
            result = Pair(false, "Confirm Password length should be greater than 6")
        } else if (!password.isEmpty() && !confirmPassword.isEmpty() && !password.equals(confirmPassword)) {
            result = Pair(false, "Password and Confirm Password should be same")
        }
        return result
    }

    fun saveAccessAndRefreshToken(accessToken: String, refreshToken: String) {
        preferenceManager.save(ACCESS_TOKEN, accessToken)
        preferenceManager.save(REFRESH_TOKEN, refreshToken)
        preferenceManager.save(IS_FIRST_TIME, true)
        preferenceManager.save(IS_LOGGED_IN, true)
    }
}