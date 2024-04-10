package com.fatherofapps.androidbase.ui.appointment.doctordetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fatherofapps.androidbase.base.viewmodel.BaseViewModel
import com.fatherofapps.androidbase.data.repositories.DoctorRepository
import com.fatherofapps.androidbase.data.response.ConfigResponse
import com.fatherofapps.androidbase.data.response.DoctorInfo
import com.fatherofapps.androidbase.helper.preferences.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoctorDetailViewModel @Inject constructor(
    private val repository: DoctorRepository,
    private val preferenceManager: PreferenceManager
) : BaseViewModel(){

    private var _doctorDetailResponse = MutableLiveData<ConfigResponse<DoctorInfo>?>()
    val doctorDetailResponse: LiveData<ConfigResponse<DoctorInfo>?>
        get() = _doctorDetailResponse

    private  val _doctorScheduleDay = MutableLiveData<ConfigResponse<List<String>>?>()
    val doctorScheduleDay: LiveData<ConfigResponse<List<String>>?>
        get() = _doctorScheduleDay

    fun getDoctorDetail(doctorId: String?) {
        showLoading(true)
        parentJob = viewModelScope.launch(handler) {
            val data = repository.getDoctorById(doctorId ?: "")
            _doctorDetailResponse.postValue(data)
        }
        registerJobFinish()
    }

    fun getDoctorScheduleDay(doctorId: String?) {
        showLoading(true)
        parentJob = viewModelScope.launch(handler) {
            val data = repository.getDoctorScheduleDay(doctorId ?: "")
            _doctorScheduleDay.postValue(data)
        }
        registerJobFinish()
    }
}