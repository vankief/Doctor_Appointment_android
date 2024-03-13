package com.fatherofapps.androidbase.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.fatherofapps.androidbase.R
import com.fatherofapps.androidbase.activities.MainActivity
import com.fatherofapps.androidbase.base.fragment.BaseFragment
import com.fatherofapps.androidbase.data.response.SpecialistDoctor
import com.fatherofapps.androidbase.data.response.TopDoctor
import com.fatherofapps.androidbase.databinding.FragmentHomeBinding
import com.fatherofapps.androidbase.helper.preferences.PreferenceManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment @Inject constructor(
) : BaseFragment() {

    private lateinit var dataBinding: FragmentHomeBinding
    private var  specialistDoctor: List<SpecialistDoctor> = emptyList()
    private var topdoctor: List<TopDoctor> = emptyList()
    private val viewModel by viewModels<HomeViewModel>()
    private var specialistAdapter: SpecialistAdapter? = null
    private var topDoctorAdapter: TopDoctorAdapter? = null


    companion object {
        private const val TAG = "HomeFragment"
    }

    @Inject
    lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getDoctorsBySpecialist()
        viewModel.getTopDoctors()

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentHomeBinding.inflate(inflater, container, false)
        dataBinding.lifecycleOwner = viewLifecycleOwner
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerAllExceptionEvent(viewModel, viewLifecycleOwner)
        registerObserverLoadingEvent(viewModel, viewLifecycleOwner)
        hideOpenTopAppBar(true)
        hideOpenNavigation(true)
        setupObservers()
        setupObserver()
        dataBinding.tvViewAllSpecialty.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSpecialistFragment()
            navigateToPage(action)
        }


    }

    private fun setupSpecialistRecyclerView() {
        specialistAdapter = SpecialistAdapter(specialistDoctor)
        dataBinding.rvSpecialty.apply {
            layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL,
                false) }
        dataBinding.rvSpecialty.adapter = specialistAdapter

    }

    private fun setupTopDoctorRecyclerView() {
        topDoctorAdapter = TopDoctorAdapter(topdoctor)
        dataBinding.rvTopDoctor.apply {
            layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL,
                false) }
        dataBinding.rvTopDoctor.adapter = topDoctorAdapter
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun setupObservers() {
        viewModel.specialistResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response == null) {
                showErrorMessage("Network error")
            } else {
                if (response.isSuccess()) {
                    Log.d(TAG, "Specialist: ${response.data}}")
                    response.data?.let { data ->
                        specialistDoctor = data
                        setupSpecialistRecyclerView()
                    }

                } else {
                    showErrorMessage(response.checkTypeErr())
                }
            }
        })
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun setupObserver() {
        viewModel.topDoctorResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response == null) {
                showErrorMessage("Network error")
            } else {
                if (response.isSuccess()) {
                    Log.d(TAG, "Top Doctor: ${response.data}}")
                    response.data?.let { data ->
                        topdoctor = data
                        setupTopDoctorRecyclerView()
                    }

                } else {
                    showErrorMessage(response.checkTypeErr())
                }
            }
        })
    }


}