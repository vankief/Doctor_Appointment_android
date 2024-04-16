package com.fatherofapps.androidbase.ui.setting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fatherofapps.androidbase.base.viewmodel.BaseViewModel
import com.fatherofapps.androidbase.common.Constants.ACCESS_TOKEN
import com.fatherofapps.androidbase.common.Constants.IS_LOGGED_IN
import com.fatherofapps.androidbase.common.Constants.REFRESH_TOKEN
import com.fatherofapps.androidbase.data.repositories.PatientRepository
import com.fatherofapps.androidbase.data.response.ConfigResponse
import com.fatherofapps.androidbase.data.response.PatientInfo
import com.fatherofapps.androidbase.helper.preferences.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val patientRepository: PatientRepository,
    private val preferenceManager: PreferenceManager
) : BaseViewModel() {

    private var _patientResponse = MutableLiveData < ConfigResponse <PatientInfo>?>()
    val patientResponse: MutableLiveData < ConfigResponse <PatientInfo>?>
        get() = _patientResponse

    fun getPatientInfo() {
        showLoading(true)
        parentJob = viewModelScope.launch(handler) {
            val data = patientRepository.getPatientById()
            _patientResponse.postValue(data)
        }
        registerJobFinish()
    }

    fun clearToken() {
        preferenceManager.clear(ACCESS_TOKEN)
        preferenceManager.clear(REFRESH_TOKEN)
        preferenceManager.clear(IS_LOGGED_IN)
    }
}