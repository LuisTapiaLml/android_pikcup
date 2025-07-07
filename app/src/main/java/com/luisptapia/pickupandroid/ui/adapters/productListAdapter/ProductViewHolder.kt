package com.luisptapia.pickupandroid.ui.adapters.productListAdapter

import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.luisptapia.pickupandroid.R
import com.luisptapia.pickupandroid.data.remote.model.OrderDetailDto
import com.luisptapia.pickupandroid.databinding.ProductElementBinding
import com.luisptapia.pickupandroid.utils.Constants
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
            tvProductEan.text = product.ean
            tvProductQuantity.text = root.context.getString(R.string.product_quantity, product.quantity.toString())

        }

        if( product.serail_numbers.isNotEmpty() ){


            val hasEmptySerial = product.serail_numbers.any { it.num_serie.isNullOrBlank() }

            Log.d(Constants.APP_NAME_LOGS, "Missing serials: $hasEmptySerial")

            var colorResId = R.color.colorWarning;

            if (hasEmptySerial) {
                colorResId = R.color.colorDanger
            } else {
                colorResId = R.color.markedColor
            }

            binding.CHSerialNumber.setChipBackgroundColorResource(colorResId)


        }else{
            binding.CHSerialNumber.visibility = View.GONE
        }

        Glide
            .with(binding.root.context)
            .load(product.product_image)
            .centerCrop()
            .into(binding.ivProductImage);
    }


}