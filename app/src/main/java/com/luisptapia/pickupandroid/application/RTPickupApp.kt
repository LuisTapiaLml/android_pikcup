package com.luisptapia.pickupandroid.application

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.luisptapia.pickupandroid.data.PickupRepository
import com.luisptapia.pickupandroid.data.remote.RetrofitHelper
import com.luisptapia.pickupandroid.utils.SessionManager

class RTPickupApp:Application() {

    lateinit var sessionManager: SessionManager
        private set


    val isLoggedInLiveData = MutableLiveData<Boolean>()


    override fun onCreate() {
        super.onCreate()
        sessionManager = SessionManager(this)

        // Set initial state
        isLoggedInLiveData.value = sessionManager.getToken() != null

    }

    val retrofit by lazy {
        RetrofitHelper(
            sessionManager = sessionManager,
            onUnauthorized = {
                // Maybe post to a LiveData to trigger logout
            }
        ).getRetrofit()
    }

    val repository by lazy {
        PickupRepository(retrofit)
    }


}