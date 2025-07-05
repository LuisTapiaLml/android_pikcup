package com.luisptapia.pickupandroid.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.luisptapia.pickupandroid.R
import com.luisptapia.pickupandroid.data.remote.model.OrderDto
import com.luisptapia.pickupandroid.databinding.FragmentOrderListBinding
import com.luisptapia.pickupandroid.ui.adapters.orderListAdapter.OrderAdapter


class OrderListFragment : Fragment() {

    private lateinit var _binding: FragmentOrderListBinding
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var orders = listOf(
            OrderDto(order_id = 1 , order_number =  "223425", status = "Asignado", order_date = "01/05/2025 23:59", customer_name = "Luis Enrique Perez"),
            OrderDto(order_id = 2 , order_number =  "234525", status = "Entregado", order_date = "30/04/2025", customer_name = "Ana Luisa Marin"),
            OrderDto(order_id = 3 , order_number =  "223425", status = "Confirmado", order_date = "20/04/2025", customer_name = "Mario Alexis Perez"),
            OrderDto(order_id = 4 , order_number =  "2234256", status = "Cancelado", order_date = "20/04/2025", customer_name = "Mario Alexis Perez"),
            OrderDto(order_id = 1 , order_number =  "223425", status = "Asignado", order_date = "01/05/2025", customer_name = "Luis Enrique Perez"),
            OrderDto(order_id = 2 , order_number =  "234525", status = "Entregado", order_date = "30/04/2025", customer_name = "Ana Luisa Marin"),
            OrderDto(order_id = 3 , order_number =  "223425", status = "Confirmado", order_date = "20/04/2025", customer_name = "Mario Alexis Perez"),
            OrderDto(order_id = 4 , order_number =  "2234256", status = "Cancelado", order_date = "20/04/2025", customer_name = "Mario Alexis Perez")

        )

        binding.rvOrdersContainer.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = OrderAdapter(orders){ selectedOrder ->

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.fcFragmentContainer,
                        OrderDetailFragment.newInstance()
                    )
                    .addToBackStack(null)
                    .commit()
            }
        }

    }





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOrderListBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            OrderListFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}