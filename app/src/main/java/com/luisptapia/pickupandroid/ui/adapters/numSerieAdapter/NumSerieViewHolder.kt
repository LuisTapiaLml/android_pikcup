package com.luisptapia.pickupandroid.ui.adapters.numSerieAdapter

import androidx.recyclerview.widget.RecyclerView
import com.luisptapia.pickupandroid.data.remote.model.OrderDto
import com.luisptapia.pickupandroid.data.remote.model.ProductSerialNumberDto
import com.luisptapia.pickupandroid.databinding.FragmentNumSerieBinding
import com.luisptapia.pickupandroid.databinding.SerialNumberElementBinding

class NumSerieViewHolder(
    private val binding: SerialNumberElementBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(numSerie: ProductSerialNumberDto,
              title : String){

        if(numSerie.serial_number == ""){
            binding.tiNumSerie.setText(numSerie.serial_number)
        }

        binding.tvProductTitle.text = title

    }

}