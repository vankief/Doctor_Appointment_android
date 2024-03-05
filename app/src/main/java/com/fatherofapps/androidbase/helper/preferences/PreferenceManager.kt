package com.fatherofapps.androidbase.helper.preferences

import android.content.Context
import android.util.Base64
import com.google.gson.Gson
class PreferenceManager(context: Context, private val gson: Gson = Gson()) {

    private val preferencesName = "doctor_app_shared_prefs"

    private val sharedPreferences =
        context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)


    fun save(@PreferenceKeys key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun save(@PreferenceKeys key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    fun save(@PreferenceKeys key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun save(@PreferenceKeys key: String, value: Long) {
        sharedPreferences.edit().putLong(key, value).apply()
    }

    fun save(@PreferenceKeys key: String, value: Float) {
        sharedPreferences.edit().putFloat(key, value).apply()
    }

    fun <T> save(@PreferenceKeys key: String, value: T) {
        val obj = gson.toJson(value)
        sharedPreferences.edit().putString(key, obj).apply()
    }

    fun getString(@PreferenceKeys key: String, defValue: String? = null): String? {
        return sharedPreferences.getString(key, "hello")
    }

    fun getInt(@PreferenceKeys key: String, defValue: Int = -1): Int {
        return sharedPreferences.getInt(key, defValue)
    }

    fun getBoolean(@PreferenceKeys key: String, defValue: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key, defValue)
    }


    fun getLong(@PreferenceKeys key: String, defValue: Long = -1L): Long {
        return sharedPreferences.getLong(key, defValue)
    }

    fun getFloat(@PreferenceKeys key: String, defValue: Float = -1f): Float {
        return sharedPreferences.getFloat(key, defValue)
    }

    fun <T> getObject(@PreferenceKeys keys: String, type: Class<T>): T? {
        val obj = sharedPreferences.getString(keys, null)
        return gson.fromJson(obj, type)
    }

    fun clear(@PreferenceKeys key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    fun saveByteArray(iv: String, second: ByteArray) {
        val encodedString = Base64.encodeToString(second, Base64.DEFAULT)
        save(iv, encodedString)
    }

    fun getByteArray(iv: String): ByteArray? {
        val encodedString = getString(iv)
        return Base64.decode(encodedString, Base64.DEFAULT)
    }

}