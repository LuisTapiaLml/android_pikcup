package com.luisptapia.pickupandroid.data.remote.model

import com.google.gson.annotations.SerializedName

data class NumSeriePayload(

    @SerializedName("ean")
    var ean: String,

    @SerializedName("serialNumbers")
    var serialNumbers: List<NumSerieItemPayload>
)

data class NumSerieItemPayload(

    @SerializedName("id")
    var id:Int,

    @SerializedName("numSerie")
    var numSerie: String,

)


data class msgDto(


    @SerializedName("msg")
    var msg: String,

    )