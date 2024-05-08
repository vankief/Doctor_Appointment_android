package com.fatherofapps.androidbase.helper.inter

interface MainActivityListener {
    fun setTitle(title: String)
    fun setNavigationIcon(iconResId: Int)
    fun onNavigationIconClicked()
}