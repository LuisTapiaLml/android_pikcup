package com.luisptapia.pickupandroid.data.remote.model

import com.google.gson.annotations.SerializedName

data class UserLoginDto(

    @SerializedName("email")
    var email:String,

    @SerializedName("password")
    var password:String

)
