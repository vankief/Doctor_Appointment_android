package com.fatherofapps.androidbase.ui.setting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.fatherofapps.androidbase.base.fragment.BaseFragment
import com.fatherofapps.androidbase.data.response.PatientInfo
import com.fatherofapps.androidbase.databinding.FragmentSettingBinding
import com.fatherofapps.androidbase.ui.specialist.SpecialistFragment
import com.fatherofapps.androidbase.utils.convertImagePath
import com.fatherofapps.androidbase.utils.convertToNormalDate
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class SettingFragment @Inject constructor(): BaseFragment() {

    private lateinit var dataBinding: FragmentSettingBinding
    private val viewModel by viewModels<SettingViewModel>()

    companion object {
        private const val TAG = "SettingFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getPatientInfo()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentSettingBinding.inflate(inflater, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideOpenTopAppBar(true)
        hideOpenNavigation(true)
        dataBinding.cardViewInvite.setOnClickListener {
            val action = SettingFragmentDirections.actionSettingFragmentToCreatCardFragment()
            navigateToPage(action)
        }
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.patientResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response == null) {
                showErrorMessage("Network error")
            } else {
                if (response.isSuccess()) {
                    Log.d(TAG, "Specialist: ${response.data}}")
                    response.data?.let { data ->
                        setupInfo(data)
                    }

                } else {
                    showErrorMessage(response.checkTypeErr())
                }
            }
        })
    }

    private fun setupInfo(data: PatientInfo) {
        dataBinding.txtName.text = data.name
        dataBinding.txtEmail.text = data.email
        dataBinding.txtDob.text = convertToNormalDate(data.dob)
        Glide.with(requireContext())
            .load(convertImagePath(data.image))
            .into(dataBinding.imgAvatar)
    }
}