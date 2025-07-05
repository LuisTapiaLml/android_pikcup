package com.luisptapia.pickupandroid.ui.adapters.productListAdapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.luisptapia.pickupandroid.R
import com.luisptapia.pickupandroid.data.remote.model.OrderDetailDto
import com.luisptapia.pickupandroid.databinding.ProductElementBinding
import java.text.NumberFormat
import java.util.Locale


class ProductViewHolder(
    private val binding: ProductElementBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(product: OrderDetailDto){

        val mexicanLocale = Locale("es", "MX") // Spanish, Mexico
        val formatter = NumberFormat.getCurrencyInstance(mexicanLocale)

        val price = formatter.format(product.unit_price)

        binding.apply {

            tvProductDescription.text = product.product_description;
            tvProductPrice.text = root.context.getString(R.string.unit_price, price)
            tvProductEan.text = product.product_ean
            tvProductQuantity.text = root.context.getString(R.string.product_quantity, product.quantity.toString())

        }

        if(!product.serial_number_required){
            binding.CHSerialNumber.visibility = View.GONE
        }

        Glide
            .with(binding.root.context)
            .load(product.product_image)
            .centerCrop()
            .into(binding.ivProductImage);
    }


}