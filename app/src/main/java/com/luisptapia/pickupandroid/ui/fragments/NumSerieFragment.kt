package com.luisptapia.pickupandroid.ui.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.os.Vibrator
import android.os.VibrationEffect
import com.luisptapia.pickupandroid.R

import android.os.VibratorManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.luisptapia.pickupandroid.data.remote.model.ProductSerialNumberDto
import com.luisptapia.pickupandroid.databinding.FragmentNumSerieBinding
import com.luisptapia.pickupandroid.ui.adapters.numSerieAdapter.NumSerieAdapter
import com.luisptapia.pickupandroid.ui.adapters.productListAdapter.ProductAdapter


class NumSerieFragment : Fragment() {

    private lateinit var _binding: FragmentNumSerieBinding
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNumSerieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btRegresar.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fcFragmentContainer,
                    OrderDetailFragment.newInstance()
                )
                .commit()
        }

        binding.btGuardar.setOnClickListener{
            vibrateDevice()
        }

        var numsSerie = listOf(
            ProductSerialNumberDto(
                order_id = 1,
                serial_number_id = 1,
                serial_number = ""
            ),
            ProductSerialNumberDto(
                order_id = 1,
                serial_number_id = 1,
                serial_number = ""
            )
        )

        binding.rvNumSerie.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = NumSerieAdapter(numsSerie){
                    Toast.makeText(context,"", Toast.LENGTH_SHORT).show()
                }

        }


    }


    companion object {

        @JvmStatic
        fun newInstance() =
            NumSerieFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    private fun vibrateDevice() {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = requireContext().getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // New vibrate method for API 26+
            vibrator.vibrate(VibrationEffect.createOneShot(120, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            // Deprecated in API 26
            @Suppress("DEPRECATION")
            vibrator.vibrate(120)
        }
    }
}