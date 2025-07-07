package com.luisptapia.pickupandroid.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.luisptapia.pickupandroid.R
import com.luisptapia.pickupandroid.application.RTPickupApp
import com.luisptapia.pickupandroid.data.PickupRepository
import com.luisptapia.pickupandroid.databinding.FragmentOrderListBinding
import com.luisptapia.pickupandroid.modelView.OrderListViewModel
import com.luisptapia.pickupandroid.ui.adapters.orderListAdapter.OrderAdapter
import com.luisptapia.pickupandroid.utils.Constants
import com.luisptapia.pickupandroid.utils.SessionManager
import kotlin.math.log


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


    @SuppressLint("ResourceAsColor")
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

        binding.tfSearch.editText?.doOnTextChanged { text, _, _, _ ->
            viewModel.setQuery(text.toString())
        }

        binding.btLogout.setOnClickListener {
            app.sessionManager.logout()
            app.isLoggedInLiveData.postValue(false)
        }

        binding.tvStore.text = app.sessionManager.getUser()

        binding.toggleButtonGroup.check(R.id.btActiveFilter)

        binding.btActiveFilter.setBackgroundColor(ContextCompat.getColor(requireContext(),  R.color.markedColor))
        binding.btActiveFilter.setTextColor(R.color.white)

        viewModel.setStatusFilter("activos")



        binding.toggleButtonGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->

            if (isChecked) {

                binding.btActiveFilter.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
                binding.btActiveFilter.setTextColor(ContextCompat.getColor(requireContext(), R.color.markedColor))

                binding.btFinishFilter.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
                binding.btFinishFilter.setTextColor(ContextCompat.getColor(requireContext(), R.color.markedColor))

                binding.btAllFilter.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
                binding.btAllFilter.setTextColor(ContextCompat.getColor(requireContext(), R.color.markedColor))

                val filter = when (checkedId) {
                    R.id.btAllFilter -> "todos"
                    R.id.btFinishFilter -> "finalizados"
                    else -> "activos"
                }



                when (checkedId) {

                    R.id.btAllFilter -> {
                        binding.btAllFilter.setBackgroundColor(ContextCompat.getColor(requireContext(),  R.color.markedColor))
                        binding.btAllFilter.setTextColor(R.color.white)
                    }
                    R.id.btFinishFilter ->{

                        binding.btFinishFilter.setBackgroundColor(ContextCompat.getColor(requireContext(),  R.color.markedColor))
                        binding.btFinishFilter.setTextColor(R.color.white)

                    }
                    R.id.btActiveFilter-> {

                        binding.btActiveFilter.setBackgroundColor(ContextCompat.getColor(requireContext(),  R.color.markedColor))
                        binding.btActiveFilter.setTextColor(R.color.white)

                    }
                }


                Log.d(Constants.APP_NAME_LOGS, "Filtro: ${filter}")

                viewModel.setStatusFilter(filter)

            }
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