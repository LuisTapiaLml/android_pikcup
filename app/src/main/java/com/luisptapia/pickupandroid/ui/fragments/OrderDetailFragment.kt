package com.luisptapia.pickupandroid.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.luisptapia.pickupandroid.R
import com.luisptapia.pickupandroid.data.remote.model.OrderDetailDto
import com.luisptapia.pickupandroid.data.remote.model.OrderDto
import com.luisptapia.pickupandroid.data.remote.model.ProductSerialNumberDto
import com.luisptapia.pickupandroid.databinding.FragmentOrderDetailBinding
import com.luisptapia.pickupandroid.ui.adapters.orderListAdapter.OrderAdapter
import com.luisptapia.pickupandroid.ui.adapters.productListAdapter.ProductAdapter


class OrderDetailFragment : Fragment() {

    private lateinit var _binding: FragmentOrderDetailBinding
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
        _binding = FragmentOrderDetailBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var order = OrderDto(
            order_id = 1 ,
            order_number =  "223425",
            status = "Asignado",
            order_date = "01/05/2025 23:59",
            customer_name = "Luis Enrique Perez",
            customer_phone = "5525414222",
            total = 106246.00,
            products = listOf(
                OrderDetailDto(
                    order_id = 122123,
                    orderDetailId = 1,
                    quantity = 2,
                    product_image = "https://ishopmx.vtexassets.com/arquivos/ids/318794-1200-auto?v=638814675847130000&width=1200&height=auto&aspect=true",
                    unit_price = 47999.00,
                    product_ean = "195949926495",
                    product_description = "MacBook Pro De 14 Pulgadas Chip M4, CPU De 10 Nucleos, GPU De 10 Nucleos, 24GB De Memoria Unificada Y Almacenamiento 1TB SSD",
                    serial_number_required = true,
                    serail_numbers = listOf(
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
                ),
                OrderDetailDto(
                    order_id = 122123,
                    orderDetailId = 1,
                    quantity = 1,
                    product_image = "https://ishopmx.vtexassets.com/arquivos/ids/309540-1200-auto?v=638693808298930000&width=1200&height=auto&aspect=true",
                    unit_price = 749.00,
                    product_ean = "195949775352",
                    product_description = "Cable De Carga USB-C De 240W (2M)",
                    serial_number_required = false,
                    serail_numbers = null
                ),
                OrderDetailDto(
                    order_id = 122123,
                    orderDetailId = 1,
                    quantity = 1,
                    product_image = "https://ishopmx.vtexassets.com/arquivos/ids/302086-1200-auto?v=638816488464700000&width=1200&height=auto&aspect=true",
                    unit_price = 9499.00,
                    product_ean = "195949926495",
                    product_description = "Apple Watch Serie 10 GPS + Cellular Con Caja De Aluminio De 42 MM Y Correa Deportiva",
                    serial_number_required = true,
                    serail_numbers = listOf(
                        ProductSerialNumberDto(
                            order_id = 1,
                            serial_number_id = 1,
                            serial_number = ""
                        )
                    )
                )
            )

        )


        binding.rvProductsList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = order.products?.let { order ->
                ProductAdapter(order){
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.fcFragmentContainer,
                            NumSerieFragment.newInstance()
                        )
                        .addToBackStack(null)
                        .commit()
                    }
                }
        }





    }

    companion object {

        @JvmStatic
        fun newInstance() =
            OrderDetailFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}