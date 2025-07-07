package com.luisptapia.pickupandroid.ui

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.luisptapia.pickupandroid.R
import com.luisptapia.pickupandroid.application.RTPickupApp
import com.luisptapia.pickupandroid.databinding.ActivityMain2Binding
import com.luisptapia.pickupandroid.databinding.ActivityMainBinding
import com.luisptapia.pickupandroid.ui.fragments.LoginFragment
import com.luisptapia.pickupandroid.ui.fragments.OrderListFragment
import com.luisptapia.pickupandroid.utils.NetworkConnectionLiveData

class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding;
    private lateinit var networkConnection: NetworkConnectionLiveData


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain2Binding.inflate(layoutInflater)

        val app = application as RTPickupApp

        enableEdgeToEdge()

        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        networkConnection = NetworkConnectionLiveData(application)

        networkConnection.observe(this) { isConnected ->
            if (!isConnected) {

                val builder = AlertDialog.Builder(this)
                    .setMessage(getString(R.string.network_connection_lost))
                    .setTitle(getString(R.string.error_title))
                    .setNegativeButton(getString(R.string.ok_button_text)) { dialog, which ->
                        dialog.dismiss()
                    }

                val dialog = builder.create()

                dialog.show()
            }
        }


        if (app.sessionManager.getToken() != null) {

            supportFragmentManager.beginTransaction()
                .replace(R.id.fcFragmentContainer, OrderListFragment())
                .commit()


            app.isLoggedInLiveData.value = true
        } else {

            supportFragmentManager.beginTransaction()
                .replace(R.id.fcFragmentContainer, LoginFragment())
                .commit()
        }


        app.isLoggedInLiveData.observe(this) { isLoggedIn ->
            if (!isLoggedIn) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fcFragmentContainer, LoginFragment())
                    .commit()
            }
        }

    }


}

