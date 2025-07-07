package com.luisptapia.pickupandroid.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.luisptapia.pickupandroid.R
import com.luisptapia.pickupandroid.application.RTPickupApp
import com.luisptapia.pickupandroid.data.PickupRepository
import com.luisptapia.pickupandroid.data.remote.model.OrderDto
import com.luisptapia.pickupandroid.databinding.FragmentOrderListBinding
import com.luisptapia.pickupandroid.modelView.OrderDetailViewModel
import com.luisptapia.pickupandroid.modelView.OrderListViewModel
import com.luisptapia.pickupandroid.ui.adapters.orderListAdapter.OrderAdapter
import com.luisptapia.pickupandroid.utils.Constants
import kotlinx.coroutines.launch
import retrofit2.HttpException


class OrderListFragment : Fragment() {

    private lateinit var _binding: FragmentOrderListBinding
    private val binding get() = _binding!!

    private lateinit var repository: PickupRepository

    private lateinit var app: RTPickupApp

    private lateinit var viewModel: OrderListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        app = requireActivity().application as RTPickupApp
        repository = (requireActivity().application as RTPickupApp).repository

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        app = requireActivity().application as RTPickupApp
        repository = (requireActivity().application as RTPickupApp).repository

        viewModel = OrderListViewModel(repository)

        binding.tvReload.setOnClickListener {
            viewModel.refresh()
        }

        binding.btReload.setOnClickListener {
            viewModel.refresh()
        }

        viewModel.orders.observe(viewLifecycleOwner) { orders ->

            binding.messageErrorContainer.visibility = View.GONE


            binding.rvOrdersContainer.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = OrderAdapter(orders) { selectedOrder ->
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.fcFragmentContainer,
                            OrderDetailFragment.newInstance(selectedOrder.id)
                        )
                        .addToBackStack(null)
                        .commit()
                }
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.rvOrdersContainer.visibility = if (isLoading) View.GONE else View.VISIBLE
            // You can show a loading spinner here if desired
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->

            if (error != null) {
                binding.messageErrorContainer.visibility = View.VISIBLE

                Log.d(Constants.APP_NAME_LOGS,"ERROR AL CARGAR ORDENES ${error}")

                error.let { Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show() }
            }
        }

        viewModel.loadOrders()

        binding.btLogout.setOnClickListener {
            app.sessionManager.logout()
            app.isLoggedInLiveData.postValue(false)
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