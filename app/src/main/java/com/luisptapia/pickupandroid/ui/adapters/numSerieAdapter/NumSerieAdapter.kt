package com.luisptapia.pickupandroid.ui.adapters.numSerieAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luisptapia.pickupandroid.data.remote.model.OrderDto
import com.luisptapia.pickupandroid.data.remote.model.ProductSerialNumberDto
import com.luisptapia.pickupandroid.databinding.OrderElementBinding
import com.luisptapia.pickupandroid.databinding.SerialNumberElementBinding
import com.luisptapia.pickupandroid.ui.adapters.orderListAdapter.OrderViewHolder

class NumSerieAdapter(
    private val numsSerie: List<ProductSerialNumberDto>,
    private val scanNumSerie: (ProductSerialNumberDto) -> Unit //
): RecyclerView.Adapter<NumSerieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumSerieViewHolder {
        val binding = SerialNumberElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NumSerieViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return  numsSerie.size
    }

    override fun onBindViewHolder(holder: NumSerieViewHolder, position: Int) {
        val numSerie = numsSerie[position]

        val title = "Art." +( position+1).toString()

        holder.bind(numSerie,title)

        //Para el click
        holder.itemView.setOnClickListener {
            scanNumSerie(numSerie)
        }
    }

}