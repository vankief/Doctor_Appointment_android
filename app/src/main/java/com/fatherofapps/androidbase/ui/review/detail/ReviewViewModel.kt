package com.fatherofapps.androidbase.ui.review.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fatherofapps.androidbase.base.viewmodel.BaseViewModel
import com.fatherofapps.androidbase.data.repositories.AppointmentRepository
import com.fatherofapps.androidbase.data.repositories.DoctorRepository
import com.fatherofapps.androidbase.data.repositories.PatientRepository
import com.fatherofapps.androidbase.data.request.createReview
import com.fatherofapps.androidbase.data.response.ConfigResponse
import com.fatherofapps.androidbase.data.response.DoctorAppointment
import com.fatherofapps.androidbase.helper.preferences.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val appointmentRepository: AppointmentRepository,
    private val patientRepository: PatientRepository,
    private val preferenceManager: PreferenceManager
) : BaseViewModel() {


    private var _doctorAppointmentResponse = MutableLiveData<ConfigResponse<DoctorAppointment>>()
    val doctorAppointmentResponse: MutableLiveData<ConfigResponse<DoctorAppointment>>
        get() = _doctorAppointmentResponse

    private var _createReviewResponse = MutableLiveData<ConfigResponse<Any>>()
    val createReviewResponse: MutableLiveData<ConfigResponse<Any>>
        get() = _createReviewResponse

    fun getDoctorAppointment(appointmentId: String) {
        showLoading(true)
        parentJob = viewModelScope.launch {
            val response = appointmentRepository.getDoctorAppointments(appointmentId)
            _doctorAppointmentResponse.postValue(response!!)
        }
        registerJobFinish()
    }

    fun createReview(review: createReview) {
        showLoading(true)
        parentJob = viewModelScope.launch {
            val response = patientRepository.createReview(review)
            _createReviewResponse.postValue(response!!)
        }
        registerJobFinish()
    }
}