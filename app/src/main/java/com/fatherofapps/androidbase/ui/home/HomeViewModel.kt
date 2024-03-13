package com.fatherofapps.androidbase.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fatherofapps.androidbase.base.viewmodel.BaseViewModel
import com.fatherofapps.androidbase.data.repositories.DoctorRepository
import com.fatherofapps.androidbase.data.repositories.SpecialistRepository
import com.fatherofapps.androidbase.data.response.ConfigResponse
import com.fatherofapps.androidbase.data.response.SpecialistDoctor
import com.fatherofapps.androidbase.data.response.TopDoctor
import com.fatherofapps.androidbase.helper.preferences.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val specialistRepository: SpecialistRepository,
    private val doctorRepository: DoctorRepository,
    private val preferenceManager: PreferenceManager
) : BaseViewModel() {

    private var _specialistResponse = MutableLiveData<ConfigResponse<List<SpecialistDoctor>>?>()
    val specialistResponse: MutableLiveData<ConfigResponse<List<SpecialistDoctor>>?>
        get() = _specialistResponse

    private var _topDoctorResponse = MutableLiveData<ConfigResponse<List<TopDoctor>>?>()
    val topDoctorResponse: MutableLiveData<ConfigResponse<List<TopDoctor>>?>
        get() = _topDoctorResponse


    fun getDoctorsBySpecialist() {
        showLoading(true)
        parentJob = viewModelScope.launch(handler) {
            val data = specialistRepository.getDoctorsBySpecialist()
            _specialistResponse.postValue(data)
        }
        registerJobFinish()
    }

    fun getTopDoctors() {
        showLoading(true)
        parentJob = viewModelScope.launch(handler) {
            val data = doctorRepository.getTopDoctors()
            Log.d("HomeViewModel", "getTopDoctors: $data")
            _topDoctorResponse.postValue(data)
        }
        registerJobFinish()
    }
}