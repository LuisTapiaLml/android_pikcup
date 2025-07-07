package com.luisptapia.pickupandroid.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SessionManager(context: Context) {

    companion object {
        private const val PREFS_NAME = "user_session"
        private const val KEY_TOKEN = "auth_token"
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    init {
        _isLoggedIn.value = getToken() != null
    }

    fun saveToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
        _isLoggedIn.postValue(true)
    }

    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)

    fun logout() {
        prefs.edit().clear().apply()
        _isLoggedIn.postValue(false)
    }
}
