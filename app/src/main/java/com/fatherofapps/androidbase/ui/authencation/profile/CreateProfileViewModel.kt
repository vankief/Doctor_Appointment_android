package com.fatherofapps.androidbase.ui.authencation.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fatherofapps.androidbase.base.viewmodel.BaseViewModel
import com.fatherofapps.androidbase.common.Constants.IS_FIRST_TIME
import com.fatherofapps.androidbase.data.models.Patient
import com.fatherofapps.androidbase.data.repositories.PatientRepository
import com.fatherofapps.androidbase.data.request.updatePatient
import com.fatherofapps.androidbase.data.response.ConfigResponse
import com.fatherofapps.androidbase.helper.preferences.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CreateProfileViewModel @Inject constructor(
    private val patientRepository: PatientRepository,
    private val preferenceManager: PreferenceManager
)  : BaseViewModel() {
    private var _createProfileResponse = MutableLiveData<ConfigResponse<Patient>>()
    val createProfileResponse: MutableLiveData<ConfigResponse<Patient>>
        get() = _createProfileResponse

    fun updateProfile(updatePatient: updatePatient) {
        showLoading(true)
        parentJob = viewModelScope.launch {
            val response = patientRepository.updatePatient(updatePatient)
            _createProfileResponse.postValue(response!!)
        }
        registerJobFinish()
    }


    fun clearIsFirstTime() {
        preferenceManager.clear(IS_FIRST_TIME)
    }
}