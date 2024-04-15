package com.fatherofapps.androidbase.ui.startui

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.fatherofapps.androidbase.R
import com.fatherofapps.androidbase.base.fragment.BaseFragment
import com.fatherofapps.androidbase.databinding.FragmentWelcomeBinding
import com.fatherofapps.androidbase.ui.comom.CommonViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeFragment : BaseFragment() {
    private lateinit var dataBinding: FragmentWelcomeBinding
    private  var isLogin: Boolean = false
    private val viewModel by activityViewModels<CommonViewModel>()


    companion object {
        private const val TAG = "WelcomeFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        isLogin = viewModel.checkLogin()
//        if(isLogin){
//            navigateToHome()
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = FragmentWelcomeBinding.inflate(inflater, container, false)
        dataBinding.lifecycleOwner = viewLifecycleOwner
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideOpenNavigation()
        hideOpenTopAppBar(false)
        dataBinding.btnSignIn.setOnClickListener {
            navigateToPage(WelcomeFragmentDirections.actionWelcomeFragmentToLoginFragment())
        }
        dataBinding.btnSignup.setOnClickListener {
            navigateToPage(WelcomeFragmentDirections.actionWelcomeFragmentToRegisterFragment())
        }

    }


    override fun onResume() {
        super.onResume()
        viewModel.checkLogined()
        Log.d(TAG, "onResume: ")
    }

    override fun onStart() {
        super.onStart()
        viewModel.checkLogined()
        Log.d(TAG, "onStart: ")
    }




    fun  navigateToHome() {
        navigateToPage(WelcomeFragmentDirections.actionWelcomeFragmentToHomeFragment())
    }
}