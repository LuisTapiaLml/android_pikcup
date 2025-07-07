package com.luisptapia.pickupandroid.data.remote.model

import com.google.gson.annotations.SerializedName

data class OrderDetailDto(

    @SerializedName("orderDetaiId")
    var order_detai_id: Int,

    @SerializedName("orderId")
    var order_id:Int,

    @SerializedName("productDescription")
    var product_description: String,

    @SerializedName("quantity")
    var quantity: Int,

    @SerializedName("unitPrice")
    var unit_price: Double,

    @SerializedName("subtotal")
    var sub_total: String,

    @SerializedName("image")
    var product_image: String,


    @SerializedName("ean")
    var ean:String,


    @SerializedName("serialNumbers")
    var serail_numbers:List<ProductSerialNumberDto>
)


