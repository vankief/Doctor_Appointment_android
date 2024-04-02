package com.fatherofapps.androidbase.ui.doctor.topdoctor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fatherofapps.androidbase.base.viewmodel.BaseViewModel
import com.fatherofapps.androidbase.data.repositories.DoctorRepository
import com.fatherofapps.androidbase.data.repositories.SpecialistRepository
import com.fatherofapps.androidbase.data.response.ConfigResponse
import com.fatherofapps.androidbase.data.response.Specialist
import com.fatherofapps.androidbase.data.response.TopDoctorBySpecialist
import com.fatherofapps.androidbase.helper.preferences.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopDoctorViewModel @Inject constructor(
    private val doctorRepository: DoctorRepository,
    private val specialistRepository: SpecialistRepository,
    private val preferenceManager: PreferenceManager
): BaseViewModel() {

    private var _topDoctorList = MutableLiveData<ConfigResponse<List<TopDoctorBySpecialist>>>()
    val topDoctorList: MutableLiveData<ConfigResponse<List<TopDoctorBySpecialist>>>
        get() = _topDoctorList
    private var _specialist = MutableLiveData<ConfigResponse<List<Specialist>>>()
    val specialist: MutableLiveData<ConfigResponse<List<Specialist>>>
        get() = _specialist
    fun getTopDoctorBySpecialist(specialist: String?) {
        showLoading(true)
        parentJob = viewModelScope.launch {
            val response = doctorRepository.getTopDoctorBySpecialist(specialist)
            _topDoctorList.postValue(response!!)
        }
        registerJobFinish()
    }

    fun getSpecialist() {
        showLoading(true)
        parentJob = viewModelScope.launch {
            val response = specialistRepository.getSpecialists()
            _specialist.postValue(response!!)
        }
        registerJobFinish()
    }

}