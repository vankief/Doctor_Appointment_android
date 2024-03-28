package com.fatherofapps.androidbase.ui.appointment.patientdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.fatherofapps.androidbase.base.fragment.BaseFragment
import com.fatherofapps.androidbase.databinding.FragmentPatientDetailBinding

class PatientDetailFragment: BaseFragment() {
    private lateinit var dataBinding: FragmentPatientDetailBinding
    private val viewModel by viewModels<PatientDetailViewModel>()
    private val ageRangeAdapter = AgeRangeAdapter(listOf("10+", "20+", "30+", "40+", "50+", "60+", "70+"))
    private var ageRange : String = ""
    private val args by navArgs<PatientDetailFragmentArgs>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentPatientDetailBinding.inflate(inflater, container, false)
        dataBinding.lifecycleOwner = viewLifecycleOwner
        dataBinding.rvAgeRange.adapter = ageRangeAdapter
        ageRangeAdapter.onItemClickListener = {ageRange ->
            this.ageRange = ageRange
        }
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideOpenNavigation(false)
        validatePatientDetail()

    }

    fun validatePatientDetail(): Pair<Boolean, String> {
        var result = Pair(true, "")
        if( ageRange.isEmpty()||
            dataBinding.edtName.text.toString().isEmpty()||
            dataBinding.edtPhone.text.toString().isEmpty()||
            dataBinding.radioGroupGender.checkedRadioButtonId == -1||
            dataBinding.edtReason.text.toString().isEmpty())
        {
            result = Pair(false, "Vui lòng điền đầy đủ thông tin")
        }
        return result
    }

}