package com.luisptapia.pickupandroid.ui.adapters.productListAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luisptapia.pickupandroid.data.remote.model.OrderDetailDto
import com.luisptapia.pickupandroid.databinding.ProductElementBinding

class ProductAdapter(

    private val products: List<OrderDetailDto>,
    private val onProductClick: (OrderDetailDto) -> Unit //Para los clicks

): RecyclerView.Adapter<ProductViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return  products.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]

        holder.bind(product)

        if(product.serail_numbers.size > 0 ){
            //Para el click
            holder.itemView.setOnClickListener {
                onProductClick(product)
            }

        }

    }
}