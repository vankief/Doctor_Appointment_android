package com.fatherofapps.androidbase.ui.doctor.topdoctor

import androidx.fragment.app.viewModels
import com.fatherofapps.androidbase.base.fragment.BaseFragment
import com.fatherofapps.androidbase.databinding.FragmentTopDoctorBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TopDoctorFragment @Inject constructor(): BaseFragment() {
    private lateinit var dataBinding: FragmentTopDoctorBinding
    private val viewModel by viewModels<TopDoctorViewModel>()


}