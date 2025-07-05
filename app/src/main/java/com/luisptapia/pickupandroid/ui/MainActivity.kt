package com.luisptapia.pickupandroid.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.luisptapia.pickupandroid.R
import com.luisptapia.pickupandroid.databinding.ActivityMainBinding
import com.luisptapia.pickupandroid.ui.fragments.LoginFragment
import com.luisptapia.pickupandroid.ui.fragments.OrderListFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fcFragmentContainer,
                LoginFragment()
            )
            .commit()
//
//        val selectedColor = getColor(R.color.markedColor)
//        val defaultColor =  getColor(R.color.white)
//        val selectedTextColor = getColor(R.color.white)
//        val defaultTextColor = getColor(R.color.markedColor)
//
//
//        binding.toggleButtonGroup.addOnButtonCheckedListener{ group, checkedId, isChecked ->
//
//            // change text color and background when color change
//            group.findViewById<MaterialButton>(checkedId)?.let { button ->
//                if (isChecked) {
//                    // Selected state
//                    button.setBackgroundColor(selectedColor)
//                    button.setTextColor(selectedTextColor)
//
//                } else {
//                    // Default state
//                    button.setBackgroundColor(defaultColor)
//                    button.setTextColor(defaultTextColor)
//
//                }
//            } // apply styles
//
//            if (isChecked) {
//                when (checkedId) {
//                    R.id.btActiveFilter -> {
//                        Toast.makeText(this,"active",Toast.LENGTH_SHORT).show()
//                    }
//                    R.id.btFinishFilter -> {
//                        Toast.makeText(this,"finish",Toast.LENGTH_SHORT).show()
//                    }
//                    R.id.btAllFilter -> {
//                        Toast.makeText(this,"all",Toast.LENGTH_SHORT).show()
//                    }
//                }
//            } // is checked
//
//        } // button change listener



    }
}