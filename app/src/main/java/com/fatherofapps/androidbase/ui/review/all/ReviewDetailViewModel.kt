package com.fatherofapps.androidbase.ui.review.all

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fatherofapps.androidbase.base.viewmodel.BaseViewModel
import com.fatherofapps.androidbase.data.repositories.DoctorRepository
import com.fatherofapps.androidbase.data.response.ConfigResponse
import com.fatherofapps.androidbase.data.response.ReViewResponse
import com.fatherofapps.androidbase.helper.preferences.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewDetailViewModel @Inject constructor(
    private val doctorRepository: DoctorRepository,
    private val preferenceManager: PreferenceManager
): BaseViewModel() {

    private var _reviewDoctor = MutableLiveData<ConfigResponse<List<ReViewResponse>>>()
    val reviewDoctor: MutableLiveData<ConfigResponse<List<ReViewResponse>>>
        get() = _reviewDoctor

    fun getReviewDoctor(doctorId: String,star: Int) {
        showLoading(true)
        parentJob = viewModelScope.launch {
            val response = doctorRepository.getDoctorReview(doctorId,star)
            _reviewDoctor.postValue(response!!)
        }
        registerJobFinish()
    }
}