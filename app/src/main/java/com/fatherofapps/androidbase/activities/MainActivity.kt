package com.fatherofapps.androidbase.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.annotation.LongDef
import androidx.annotation.OptIn
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.fatherofapps.androidbase.R
import com.fatherofapps.androidbase.base.activities.BaseActivity
import com.fatherofapps.androidbase.ui.notification.NotificationViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity(){

    private var loadingLayout: FrameLayout? = null
    private lateinit var navController: NavController
    private lateinit var materialToolbar: MaterialToolbar
    private val viewModel by viewModels<NotificationViewModel>()
    private lateinit var badge: BadgeDrawable
    var countNotification = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e("Frank","MainActivity")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getNotificationCount()
        materialToolbar = findViewById(R.id.topAppBar)
        badge = BadgeDrawable.create(this)
        setSupportActionBar(materialToolbar)
        loadingLayout = findViewById(R.id.loadingLayout)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        navController = navHostFragment.navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homeFragment -> {
                    if (navController.currentDestination?.id != R.id.homeFragment) {
                        navController.navigate(R.id.homeFragment)
                    }
                    true
                }
                R.id.appointmentFragment -> {
                    if (navController.currentDestination?.id != R.id.appointmentFragment) {
                        navController.navigate(R.id.appointmentFragment)
                    }
                    true
                }
                R.id.settingFragment -> {
                    if (navController.currentDestination?.id != R.id.settingFragment) {
                        navController.navigate(R.id.settingFragment)
                    }
                    true
                }
                else -> false
            }
        }
    }

    override fun showLoading(isShow: Boolean) {
        loadingLayout?.visibility = if(isShow) View.VISIBLE else View.GONE
    }

    @SuppressLint("UnsafeOptInUsageError")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        BadgeUtils.attachBadgeDrawable(badge, materialToolbar, R.id.notificationFragment)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.notificationFragment -> {
                //chuyển đến màn hình danh sách con
                navController.navigate(R.id.notificationFragment)
                true
            }

            R.id.favoriteDoctorFragment -> {
                //chuyển đến màn hình thêm con
                navController.navigate(R.id.favoriteDoctorFragment)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getNotificationCount() {
        viewModel.countNotificationResponse.observe(this) { response ->
            response?.let { notificationResponse ->
                if (notificationResponse != null && notificationResponse.isSuccess()) {
                    countNotification = notificationResponse.data!!
                    updateBadge()
                } else {
                    showDialogMaterial("Thông báo lỗi", notificationResponse.message ?: "Lỗi không xác định")
                }
            }
        }
    }

    override fun onStart() {
        Log.e("Frank","onStart")
        super.onStart()
        getNotificationCount()
    }

    override fun onResume() {
        Log.e("Frank","onResume")
        super.onResume()
        getNotificationCount()
    }
    fun updateBadge() {
        if (countNotification > 0) {
            badge.number = countNotification
            badge.isVisible = true
        } else {
            badge.isVisible = false
        }
    }
}