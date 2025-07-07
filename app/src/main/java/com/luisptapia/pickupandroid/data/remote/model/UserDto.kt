package com.luisptapia.pickupandroid.data.remote.model

import com.google.gson.annotations.SerializedName

data class UserDto(

    @SerializedName("id")
    var id: Int,

    @SerializedName("name")
    var name: String,

    @SerializedName("email")
    var email: String,

    @SerializedName("token")
    var token: String,


)
