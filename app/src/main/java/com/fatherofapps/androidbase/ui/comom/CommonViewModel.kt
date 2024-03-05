package com.fatherofapps.androidbase.ui.comom

import com.fatherofapps.androidbase.base.viewmodel.BaseViewModel
import com.fatherofapps.androidbase.common.Constants.ACCESS_TOKEN
import com.fatherofapps.androidbase.common.Constants.IS_FIRST_TIME
import com.fatherofapps.androidbase.common.Constants.IS_LOGGED_IN
import com.fatherofapps.androidbase.common.Constants.REFRESH_TOKEN
import com.fatherofapps.androidbase.helper.preferences.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class CommonViewModel @Inject constructor(private val preferenceManager: PreferenceManager) : BaseViewModel() {
    fun checkLogin(): Boolean {
        return preferenceManager.getBoolean(IS_LOGGED_IN, false)
    }
    fun clearToken() {
        preferenceManager.clear(ACCESS_TOKEN)
        preferenceManager.clear(REFRESH_TOKEN)
    }

    fun getIsFirstTime(): Boolean {
        return preferenceManager.getBoolean(IS_FIRST_TIME, true)
    }

    fun setIsFirstTime(isFirstTime: Boolean) {
        preferenceManager.save(IS_FIRST_TIME, isFirstTime)
    }
}