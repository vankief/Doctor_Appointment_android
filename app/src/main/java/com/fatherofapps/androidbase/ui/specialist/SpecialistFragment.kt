package com.fatherofapps.androidbase.ui.specialist

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.fatherofapps.androidbase.activities.MainActivity
import com.fatherofapps.androidbase.base.fragment.BaseFragment
import com.fatherofapps.androidbase.data.response.SpecialistDoctor
import com.fatherofapps.androidbase.databinding.FragmentSpecialistDoctorBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SpecialistFragment @Inject constructor() : BaseFragment() {

    private lateinit var dataBinding: FragmentSpecialistDoctorBinding
    private var  specialistDoctor: List<SpecialistDoctor> = emptyList()
    private val viewModel by viewModels<SpecialistViewModel>()
    private var specialistAdapter: SpecialistAdapter? = null

    companion object {
        private const val TAG = "SpecialistFragment"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getDoctorsBySpecialist()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentSpecialistDoctorBinding.inflate(inflater, container, false)
        dataBinding.lifecycleOwner = viewLifecycleOwner
        (activity as MainActivity).setTitle("Chuyên Khoa")
        (activity as MainActivity).setNavigationBackIcon()
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerAllExceptionEvent(viewModel, viewLifecycleOwner)
        registerObserverLoadingEvent(viewModel, viewLifecycleOwner)
        hideOpenTopAppBar(true)
        hideOpenNavigation(true)
        setupObservers()
    }
    private fun setupSpecialistRecyclerView() {
        specialistAdapter = SpecialistAdapter(specialistDoctor)
        dataBinding.rvSpecialistDoctor.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = specialistAdapter
        }
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun setupObservers() {
        viewModel.specialistResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response != null && response.isSuccess()){
                response.data?.let { data ->
                    specialistDoctor = data
                    setupSpecialistRecyclerView()
                }
            } else {
                if (response==null) showErrorMessage("Lỗi Mạng")
                else showErrorMessage(response.checkTypeErr())
            }
        })
    }
    }
