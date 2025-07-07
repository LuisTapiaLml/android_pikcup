package com.luisptapia.pickupandroid.data.remote.model

import com.google.gson.annotations.SerializedName

data class ProductSerialNumberDto(

    @SerializedName("id")
    var id: Int,


    @SerializedName("orderDetaiId")
    var order_detaiId:Int,


    @SerializedName("ean")
    var ean:String,

    @SerializedName("numSerie")
    var num_serie:String,

)
