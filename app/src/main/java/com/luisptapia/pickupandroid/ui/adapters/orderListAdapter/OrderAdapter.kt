package com.luisptapia.pickupandroid.ui.adapters.orderListAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luisptapia.pickupandroid.data.remote.model.OrderDto
import com.luisptapia.pickupandroid.databinding.OrderElementBinding

class OrderAdapter(

    private val orders: List<OrderDto>, //Los juegos a desplegar
    private val onOrderClick: (OrderDto) -> Unit //Para los clicks

): RecyclerView.Adapter<OrderViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {

        val binding = OrderElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]

        holder.bind(order)

        //Para el click
        holder.itemView.setOnClickListener {
            onOrderClick(order)
        }
    }

}