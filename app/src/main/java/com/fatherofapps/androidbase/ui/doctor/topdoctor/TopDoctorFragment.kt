package com.fatherofapps.androidbase.ui.doctor.topdoctor

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.fatherofapps.androidbase.base.fragment.BaseFragment
import com.fatherofapps.androidbase.data.response.Specialist
import com.fatherofapps.androidbase.data.response.TopDoctorBySpecialist
import com.fatherofapps.androidbase.databinding.FragmentTopDoctorBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TopDoctorFragment @Inject constructor(): BaseFragment() {
    private lateinit var dataBinding: FragmentTopDoctorBinding
    private val viewModel by viewModels<TopDoctorViewModel>()
    private var specialist: List<Specialist> = emptyList()
    private var topDoctorList: List<TopDoctorBySpecialist> = emptyList()
    private var specialistAdapter : SpecialistAdapter? = null
    private var topDoctorAdapter : TopDoctorAdapter? = null
    private val args: TopDoctorFragmentArgs by navArgs()

    companion object {
        private const val TAG = "TopDoctorFragment"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getSpecialist()
        viewModel.getTopDoctorBySpecialist(args.specialistId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentTopDoctorBinding.inflate(inflater, container, false)
        dataBinding.lifecycleOwner = viewLifecycleOwner
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerAllExceptionEvent(viewModel, viewLifecycleOwner)
        registerObserverLoadingEvent(viewModel, viewLifecycleOwner)
        hideOpenTopAppBar(true)
        hideOpenNavigation(false)
        setupObservers()
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun setupObservers() {
        viewModel.specialist.observe(viewLifecycleOwner, Observer { response ->
                if (response!= null && response.isSuccess()) {
                    Log.d(TAG, "Specialist: ${response.data}}")
                    response.data?.let { data ->
                        specialist = data
                        setupSpecialistRecyclerView()

                }}
                else {
                    if (response == null) showErrorMessage("Lỗi mạng")
                    else showErrorMessage(response.checkTypeErr())
            }
        })
        viewModel.topDoctorList.observe(viewLifecycleOwner, Observer { response ->
            if (response!= null && response.isSuccess()) {
                Log.d(TAG, "TopDoctor: ${response.data}}")
                response.data?.let { data ->
                    topDoctorList = data
                    setupTopDoctorRecyclerView()
                }
            }
            else {
                if (response == null) showErrorMessage("Lỗi mạng")
                else showErrorMessage(response.checkTypeErr())
            }
        })

    }

    private fun setupSpecialistRecyclerView() {
        specialistAdapter = SpecialistAdapter(specialist, args.specialistId)
        dataBinding.rvSpecialty.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        // Xử lý sự kiện khi chọn một chuyên gia
        specialistAdapter?.onItemClickListener = { selectedSpecialistId ->
            viewModel.getTopDoctorBySpecialist(selectedSpecialistId)
        }
        dataBinding.rvSpecialty.adapter = specialistAdapter
    }



    private fun setupTopDoctorRecyclerView() {
        topDoctorAdapter = TopDoctorAdapter(topDoctorList)
        dataBinding.resultRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        dataBinding.resultRecyclerView.adapter = topDoctorAdapter
        topDoctorAdapter?.notifyDataSetChanged()
    }

}