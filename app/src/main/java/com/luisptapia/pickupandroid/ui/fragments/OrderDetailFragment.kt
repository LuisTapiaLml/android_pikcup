package com.luisptapia.pickupandroid.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.luisptapia.pickupandroid.R
import com.luisptapia.pickupandroid.application.RTPickupApp
import com.luisptapia.pickupandroid.data.PickupRepository
import com.luisptapia.pickupandroid.databinding.FragmentOrderDetailBinding
import com.luisptapia.pickupandroid.modelView.OrderDetailViewModel
import com.luisptapia.pickupandroid.ui.adapters.productListAdapter.ProductAdapter
import com.luisptapia.pickupandroid.utils.Constants
import com.luisptapia.pickupandroid.utils.DialogUtils
import com.luisptapia.pickupandroid.utils.formatDate
import retrofit2.HttpException
import java.text.NumberFormat
import java.util.Locale


class OrderDetailFragment : Fragment() {

    private val ARG_ORDER_ID = "order_id"
    private var orderId: Int = 0

    private lateinit var _binding: FragmentOrderDetailBinding
    private val binding get() = _binding!!


    private lateinit var app: RTPickupApp
    private lateinit var repository: PickupRepository

    private lateinit var viewModel: OrderDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            orderId = it.getInt(ARG_ORDER_ID)
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

        val mexicanLocale = Locale("es", "MX") // Spanish, Mexico
        val formatter = NumberFormat.getCurrencyInstance(mexicanLocale)

        Log.d(Constants.APP_NAME_LOGS,"orderid: ${orderId}")

        app = requireActivity().application as RTPickupApp
        repository = (requireActivity().application as RTPickupApp).repository

        viewModel = OrderDetailViewModel(repository)

        viewModel.loadOrder(orderId)


        binding.tvReload.setOnClickListener {
            viewModel.refresh(orderId)
        }

        binding.btConfirmar

        viewModel.order.observe(viewLifecycleOwner) { order ->



            binding.messageErrorContainer.visibility = View.GONE
            binding.viewContainer.visibility= View.VISIBLE

            if(order == null){

                Log.d(Constants.APP_NAME_LOGS,"ORDEN NO SE ENCONTRO")
                binding.messageErrorContainer.visibility = View.GONE
                binding.viewContainer.visibility= View.VISIBLE

            }else{

                if(order.status == "Asignado" ){
                    binding.apply {
                        btEntregar.visibility = View.GONE
                        btRechazar.visibility = View.VISIBLE
                        btConfirmar.visibility = View.VISIBLE
                    }
                }else if (order.status == "Confirmado"){
                    binding.apply {
                        btEntregar.visibility = View.VISIBLE
                        btRechazar.visibility = View.GONE
                        btConfirmar.visibility = View.GONE
                    }
                }else{
                    binding.apply {
                        btEntregar.visibility = View.GONE
                        btRechazar.visibility = View.GONE
                        btConfirmar.visibility = View.GONE
                    }
                }

                var colorRes = when(order.status.lowercase()){
                    "asignado" -> R.color.colorWarning

                    "confirmado" -> R.color.colorSuccess

                    "entregado" -> R.color.markedColor

                    "cancelado" -> R.color.colorDanger

                    else -> R.color.colorDanger
                }


                Log.d(Constants.APP_NAME_LOGS,order.status)

                val chipTextColor = when(order.status.lowercase()){
                    "asignado" -> R.color.black

                    "confirmado" -> R.color.white

                    "entregado" -> R.color.white

                    "cancelado" -> R.color.white

                    else -> R.color.colorDanger
                }

                binding.apply {
                    tvOrderNumber.text = order.id.toString()

                    tvEmail.text = order.email
                    tvCustomerName.text = order.customer_name
                    tvPhone.text = order.phone
                    tvOrderDate.text = formatDate(order.order_date)
                    tvTotal.text = formatter.format(order.total)
                    chStatus.text = order.status

                    chStatus.setChipBackgroundColorResource(colorRes)
                    chStatus.setTextColor(ContextCompat.getColor(requireContext(),chipTextColor))

                }

                binding.rvProductsList.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = order?.products?.let { order ->
                        ProductAdapter(order){ product ->
                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(
                                    R.id.fcFragmentContainer,
                                    NumSerieFragment.newInstance(orderId,product.ean)
                                )
                                .addToBackStack(null)
                                .commit()
                        }
                    }
                }

                binding.btRechazar.setOnClickListener{
                    try {

                        DialogUtils.showDialog(
                            context = requireContext(),
                            title = getString(R.string.rechazar_pedido),
                            message = getString(R.string.seguro_que_desea_rechazar_el_pedido),
                            positiveButtonTitle = getString(R.string.cancelar) ,
                            negativeButtonTitle = getString(R.string.recahzar_pedido),
                            onNegativeClick = {
                                viewModel.updateOrderStatus(orderId, getString(R.string.cancelado))

                                viewModel.refresh(orderId)
                            }

                        )

                    }catch (e:HttpException){
                        Log.d(Constants.APP_NAME_LOGS,e.message.toString())
                    }catch (e:Exception){
                        Log.d(Constants.APP_NAME_LOGS,e.message.toString())
                    }
                }

                binding.btEntregar.setOnClickListener{
                    try {

                        DialogUtils.showDialog(
                            context = requireContext(),
                            title = getString(R.string.entregar_pedido),
                            message = getString(R.string.seguro_que_desea_entregar_el_pedido),
                            positiveButtonTitle = getString(R.string.cancelar),
                            negativeButtonTitle =getString(R.string.entregar_pedido),
                            onNegativeClick = {
                                viewModel.updateOrderStatus(orderId, getString(R.string.entregado))

                                viewModel.refresh(orderId)
                            }

                        )

                    }catch (e:HttpException){
                        Log.d(Constants.APP_NAME_LOGS,e.message.toString())
                    }catch (e:Exception){
                        Log.d(Constants.APP_NAME_LOGS,e.message.toString())
                    }
                }

            } // else order exists

            binding.btConfirmar.setOnClickListener{
                try {

                    val hasEmptySerial = order?.products
                        ?.flatMap { it.serail_numbers }
                        ?.any { it.num_serie.isNullOrBlank() } ?: false

                    if (hasEmptySerial) {

                        DialogUtils.showDialog(
                            context = requireContext(),
                            title = "Error",
                            message = "Uno o mas productos necesitan número de serie asignado"
                        )

                        return@setOnClickListener
                    }

                    DialogUtils.showDialog(
                        context = requireContext(),
                        title = "Confirmar Pedido",
                        message = "¿Seguro que desea Confirmar el pedido?",
                        positiveButtonTitle = "Cancelar",
                        negativeButtonTitle ="Confirmar Pedido",
                        onNegativeClick = {

                            viewModel.updateOrderStatus(orderId,"Confirmado")

                            viewModel.refresh(orderId)
                        }

                    )

                }catch (e:HttpException){
                    Log.d(Constants.APP_NAME_LOGS,e.message.toString())
                }catch (e:Exception){
                    Log.d(Constants.APP_NAME_LOGS,e.message.toString())
                }
            }


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
        fun newInstance(orderId: Int) =
            OrderDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_ORDER_ID, orderId)
                }
            }
    }
}