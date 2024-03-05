package com.fatherofapps.androidbase.ui.authencation.login

import android.text.TextUtils
import androidx.appcompat.widget.ThemedSpinnerAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.load.engine.Resource
import com.fatherofapps.androidbase.base.viewmodel.BaseViewModel
import com.fatherofapps.androidbase.common.Constants.ACCESS_TOKEN
import com.fatherofapps.androidbase.common.Constants.IS_LOGGED_IN
import com.fatherofapps.androidbase.common.Constants.REFRESH_TOKEN
import com.fatherofapps.androidbase.common.Utils
import com.fatherofapps.androidbase.data.repositories.AuthRepository
import com.fatherofapps.androidbase.data.request.AuthRequest
import com.fatherofapps.androidbase.data.response.AuthResponse
import com.fatherofapps.androidbase.data.response.ConfigResponse
import com.fatherofapps.androidbase.helper.preferences.PreferenceKeys
import com.fatherofapps.androidbase.helper.preferences.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferenceManager: PreferenceManager
) : BaseViewModel() {
        private var _loginResponse = MutableLiveData<ConfigResponse<AuthResponse>>()
        val loginResponse: MutableLiveData<ConfigResponse<AuthResponse>>
            get() = _loginResponse

    fun doLogin(req: AuthRequest) {
        showLoading(true)
        parentJob = viewModelScope.launch {
            val response = authRepository.doLogin(req)
            _loginResponse.postValue(response!!)
        }
         registerJobFinish()
        }
    fun validateCredentials(
        emailAddress: String, password: String,
    ): Pair<Boolean, String> {
        var result = Pair(true, "")
        if (TextUtils.isEmpty(emailAddress) || TextUtils.isEmpty(password)) {
            result = Pair(false, "Please provide the credentials")
        } else if (!Utils.validateEmail(emailAddress)) {
            result = Pair(false, "Email is invalid")
        } else if (!TextUtils.isEmpty(password) && password.length <= 5) {
            result = Pair(false, "Password length should be greater than 5")
        }
        return result
    }
    fun checkLogin(): Boolean {
        return preferenceManager.getBoolean(IS_LOGGED_IN, false)
    }
    fun saveAccessTokenAndRefreshToken(accessToken: String, refreshToken: String) {
        preferenceManager.save(ACCESS_TOKEN, accessToken)
        preferenceManager.save(REFRESH_TOKEN, refreshToken)
        preferenceManager.save(IS_LOGGED_IN, true)
    }
    }
