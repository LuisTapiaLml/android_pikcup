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
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.luisptapia.pickupandroid.application.RTPickupApp
import com.luisptapia.pickupandroid.data.PickupRepository
import com.luisptapia.pickupandroid.data.remote.model.NumSerieItemPayload
import com.luisptapia.pickupandroid.data.remote.model.NumSeriePayload
import com.luisptapia.pickupandroid.data.remote.model.ProductSerialNumberDto
import com.luisptapia.pickupandroid.databinding.FragmentNumSerieBinding
import com.luisptapia.pickupandroid.databinding.SerialNumberElementBinding
import com.luisptapia.pickupandroid.modelView.OrderDetailViewModel
import com.luisptapia.pickupandroid.ui.adapters.numSerieAdapter.NumSerieAdapter
import com.luisptapia.pickupandroid.ui.adapters.productListAdapter.ProductAdapter
import com.luisptapia.pickupandroid.utils.Constants
import com.luisptapia.pickupandroid.utils.DialogUtils
import com.luisptapia.pickupandroid.utils.formatDate
import kotlinx.coroutines.launch
import retrofit2.HttpException


class NumSerieFragment : Fragment() {

    private val ARG_ORDER_ID = "order_id"
    private var orderId: Int = 0

    private val ARG_EAN = "ean"
    private var ean: String = ""


    private lateinit var _binding: FragmentNumSerieBinding
    private val binding get() = _binding!!

    private lateinit var app: RTPickupApp
    private lateinit var repository: PickupRepository

    private lateinit var viewModel: OrderDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            orderId = it.getInt(ARG_ORDER_ID)
            ean = it.getString(ARG_EAN).toString()
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

        app = requireActivity().application as RTPickupApp
        repository = (requireActivity().application as RTPickupApp).repository

        viewModel = OrderDetailViewModel(repository)

        viewModel.loadOrder(orderId)

        binding.btRegresar.setOnClickListener {

            requireActivity().supportFragmentManager.popBackStack()

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fcFragmentContainer,
                    OrderDetailFragment.newInstance(orderId = orderId)
                )

                .commit()
        }

        binding.tvReload.setOnClickListener {
            viewModel.refresh(orderId)
        }

        viewModel.order.observe(viewLifecycleOwner) { order ->

            binding.messageErrorContainer.visibility = View.GONE
            binding.viewContainer.visibility= View.VISIBLE

            if(order == null){

                Log.d(Constants.APP_NAME_LOGS,"ORDEN NO SE ENCONTRO")

                binding.messageErrorContainer.visibility = View.GONE
                binding.viewContainer.visibility= View.VISIBLE

            }else{

                var products = order.products.filter { product -> product.ean == ean }

                if( products.isEmpty()  ){

                    binding.messageErrorContainer.visibility = View.GONE
                    binding.viewContainer.visibility= View.VISIBLE

                }else{

                    var product = products[0];

                    val isEditable = order.status == "Asignado"

                    binding.apply {
                        tvProductDescription.text = product.product_description
                        tvProductEan.text = product.ean
                        tvProductQuantity.text =
                            getString(R.string.cantidad, product.quantity.toString())
                    }

                    if(!isEditable){
                        binding.btGuardar.visibility = View.GONE

                    }else{
                        binding.btGuardar.visibility = View.VISIBLE
                    }

                    Glide
                        .with(binding.root.context)
                        .load(product.product_image)
                        .centerCrop()
                        .into(binding.ivProductImage);


                    binding.btGuardar.setOnClickListener{
                        val updatedSerials = mutableListOf<NumSerieItemPayload>()

                        for (i in 0 until binding.containerNumSeries.childCount) {
                            val view = binding.containerNumSeries.getChildAt(i)
                            val bindingItem = SerialNumberElementBinding.bind(view)
                            val serialId = bindingItem.tiNumSerie.tag as? Int
                            val serialText = bindingItem.tiNumSerie.text.toString()

                            if (serialId != null) {
                                updatedSerials.add(
                                    NumSerieItemPayload(
                                        id = serialId,
                                        numSerie = serialText
                                    )
                                )
                            }
                        }

                        Log.d(Constants.APP_NAME_LOGS,"Num Series: ${updatedSerials}")

                        var payload = NumSeriePayload(
                            ean = product.ean,
                            serialNumbers = updatedSerials
                        )



                        lifecycleScope.launch {
                            try {
                                var response = viewModel.updateNumSerie(orderId,payload)

                            }catch ( e: HttpException ){

                                Log.d(Constants.APP_NAME_LOGS,"HTTP EXCEPTION: ${e.message.toString()}")

                            }catch (e:Exception){
                                Log.d(Constants.APP_NAME_LOGS,"HTTP EXCEPTION: ${e.message.toString()}")

                            }finally {

                                requireActivity().supportFragmentManager.popBackStack()

                                requireActivity().supportFragmentManager.beginTransaction()
                                    .replace(
                                        R.id.fcFragmentContainer,
                                        OrderDetailFragment.newInstance(orderId = orderId)
                                    )

                                    .commit()

                            }
                        }

                    }



                    Log.d(Constants.APP_NAME_LOGS,"Serial Numbers: ${product.serail_numbers.size}")

//                    binding.rvNumSerie.apply {
//                        layoutManager = LinearLayoutManager(requireContext())
//                        adapter = NumSerieAdapter(product.serail_numbers){
//                            Toast.makeText(context,"", Toast.LENGTH_SHORT).show()
//                        }
//
//                    }

                    val inflater = layoutInflater
                    binding.containerNumSeries.removeAllViews()




                    product.serail_numbers.forEachIndexed { index, serialNumberDto ->

                        val itemBinding = SerialNumberElementBinding.inflate(inflater)

                        val title = "Art. ${index + 1}"
                        itemBinding.tvProductTitle.text = title

                        itemBinding.tiNumSerie.setText(serialNumberDto.num_serie)

                        itemBinding.tiNumSerie.tag = serialNumberDto.id

                        itemBinding.tiNumSerie.isEnabled = isEditable

                        // Add to container
                        binding.containerNumSeries.addView(itemBinding.root)
                    }


                } // products found


            } // else order exists



        } // order observe


        viewModel.errorMessage.observe(viewLifecycleOwner) { msg ->
            if (msg != null) {
                binding.viewContainer.visibility= View.GONE
                binding.messageErrorContainer.visibility = View.VISIBLE

                Log.d(Constants.APP_NAME_LOGS,"ERROR AL CARGAR DETALLE ${msg}")

                msg.let { Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show() }
            }
        }// error message

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.pbLoading.visibility = if (loading) View.VISIBLE else View.GONE
        }// loading


    }


    companion object {

        @JvmStatic
        fun newInstance(orderId: Int, ean : String) =
            NumSerieFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_ORDER_ID, orderId)
                    putString(ARG_EAN, ean)
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