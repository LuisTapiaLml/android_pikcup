package com.luisptapia.pickupandroid.data.remote.model

import com.google.gson.annotations.SerializedName

data class OrderDto(

    @SerializedName("id")
    var id:Int,

    @SerializedName("userId")
    var user_id:Int,

    @SerializedName("status")
    var status: String,

    @SerializedName("email")
    var email: String,

    @SerializedName("createdAt")
    var order_date:String,

    @SerializedName("customer_name")
    var customer_name: String,

    @SerializedName("phone")
    var phone: String? =null,

    @SerializedName("totalAmount")
    var total: Double? =null,

    @SerializedName("orderDetails")
    var products:List<OrderDetailDto>
)
