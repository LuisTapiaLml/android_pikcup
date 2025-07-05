package com.luisptapia.pickupandroid.data.remote.model

import com.google.gson.annotations.SerializedName

data class OrderDto(

    @SerializedName("orderid")
    var order_id:Int,

    @SerializedName("order_number")
    var order_number:String,

    @SerializedName("status")
    var status: String,

    @SerializedName("createdAt")
    var order_date:String,

    @SerializedName("customer_name")
    var customer_name: String,

    @SerializedName("customerPhone")
    var customer_phone: String? =null,

    @SerializedName("total")
    var total: Double? =null,

    @SerializedName("products")
    var products:List<OrderDetailDto> ? =null
)
