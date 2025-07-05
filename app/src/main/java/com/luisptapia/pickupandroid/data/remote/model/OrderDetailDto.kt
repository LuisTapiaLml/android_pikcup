package com.luisptapia.pickupandroid.data.remote.model

import com.google.gson.annotations.SerializedName

data class OrderDetailDto(

    @SerializedName("id")
    var orderDetailId: Int,

    @SerializedName("orderid")
    var order_id:Int,

    @SerializedName("productDescription")
    var product_description: String,

    @SerializedName("quantity")
    var quantity: Int,

    @SerializedName("unitPrice")
    var unit_price: Double,

    @SerializedName("image")
    var product_image: String,


    @SerializedName("productEan")
    var product_ean:String,

    @SerializedName("serialNumberRequired")
    var serial_number_required:Boolean,

    @SerializedName("productsSerialNumber")
    var serail_numbers:List<ProductSerialNumberDto>?=null
)


