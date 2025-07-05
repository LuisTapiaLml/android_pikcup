package com.luisptapia.pickupandroid.data.remote.model

import com.google.gson.annotations.SerializedName

data class ProductSerialNumberDto(

    @SerializedName("id")
    var serial_number_id: Int,


    @SerializedName("orderid")
    var order_id:Int,


    @SerializedName("serialNumber")
    var serial_number:String



    )
