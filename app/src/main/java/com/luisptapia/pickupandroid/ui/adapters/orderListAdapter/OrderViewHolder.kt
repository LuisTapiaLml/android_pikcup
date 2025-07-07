package com.luisptapia.pickupandroid.ui.adapters.orderListAdapter

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.luisptapia.pickupandroid.data.remote.model.OrderDto
import com.luisptapia.pickupandroid.databinding.OrderElementBinding
import com.luisptapia.pickupandroid.R
import com.luisptapia.pickupandroid.utils.formatDate

class OrderViewHolder(
    private val binding:OrderElementBinding
)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(order: OrderDto){

        //Vinculamos las vistas con la información correspondiente
        var colorRes = when(order.status){
            "Asignado" -> R.color.colorWarning

            "Confirmado" -> R.color.colorSuccess

            "Entregado" -> R.color.markedColor

            "Cancelado" -> R.color.colorDanger

            else -> R.color.colorDanger
        }




        val chipTextColor = when(order.status){
            "Asignado" -> R.color.black

            "Confirmado" -> R.color.white

            "Entregado" -> R.color.white

            "Cancelado" -> R.color.white

            else -> R.color.colorPrimary
        }

        val textColor = ContextCompat.getColor(binding.root.context, chipTextColor)


        binding.apply {
            CHStatus.text = order.status
            binding.tvOrderDate.text = formatDate(order.order_date)
            binding.tvOrderNumber.text = order.id.toString()
            binding.tvCustomerName.text = order.customer_name



            CHStatus.setChipBackgroundColorResource(colorRes)


            CHStatus.setTextColor(textColor)


        }

    }

}