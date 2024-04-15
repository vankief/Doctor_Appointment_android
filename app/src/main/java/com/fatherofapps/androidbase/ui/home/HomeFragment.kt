package com.fatherofapps.androidbase.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.fatherofapps.androidbase.R
import com.fatherofapps.androidbase.activities.MainActivity
import com.fatherofapps.androidbase.base.fragment.BaseFragment
import com.fatherofapps.androidbase.data.request.registerNotification
import com.fatherofapps.androidbase.data.response.SpecialistDoctor
import com.fatherofapps.androidbase.data.response.TopDoctor
import com.fatherofapps.androidbase.databinding.FragmentHomeBinding
import com.fatherofapps.androidbase.helper.preferences.PreferenceManager
import com.fatherofapps.androidbase.ui.startui.WelcomeFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
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

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(requireContext(), "Notifications permission granted", Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(
                requireContext(),
                "FCM can't post notifications without POST_NOTIFICATIONS permission",
                Toast.LENGTH_LONG,
            ).show()
        }
    }

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
        Firebase.messaging.token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            val registerNotification = registerNotification(token.toString())

            viewModel.registerNotification(registerNotification)
            handlerRegisterNotification()
        })
        askNotificationPermission()
        setupObservers()
        setupObserver()
        dataBinding.tvViewAllSpecialty.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSpecialistFragment()
            navigateToPage(action)
        }

        dataBinding.tvViewAllTopDoctor.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToFragmentTopDoctor("all")
            navigateToPage(action)
        }


    }

    private fun handlerRegisterNotification() {
        viewModel.notificationResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response != null && response.isSuccess()) {
                response.data?.let {
                    Log.d(TAG, "Notification: $it")
                }
            } else {
                if (response == null) showErrorMessage("Lỗi mạng")
                else showErrorMessage(response.checkTypeErr())
            }
        })
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
            if(response!= null && response.isSuccess()){
                response.data?.let { data ->
                    specialistDoctor = data
                    setupSpecialistRecyclerView()
                }}
            else {
                if (response == null) showErrorMessage("Lỗi mạng")
                else showErrorMessage(response.checkTypeErr())
            }
        })
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun setupObserver() {
        viewModel.topDoctorResponse.observe(viewLifecycleOwner, Observer { response ->
            if(response!= null && response.isSuccess()){
                response.data?.let { data ->
                    topdoctor = data
                    setupTopDoctorRecyclerView()
                }}
            else {
                if (response == null) showErrorMessage("Lỗi mạng")
                else showErrorMessage(response.checkTypeErr())
            }
        })
    }
    private fun askNotificationPermission() {
        // This is only necessary for API Level > 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }


}