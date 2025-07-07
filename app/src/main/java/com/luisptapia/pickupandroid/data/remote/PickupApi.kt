package com.luisptapia.pickupandroid.data.remote

import com.luisptapia.pickupandroid.data.remote.model.NumSeriePayload
import com.luisptapia.pickupandroid.data.remote.model.OrderDto
import com.luisptapia.pickupandroid.data.remote.model.StatusUpdateRequest
import com.luisptapia.pickupandroid.data.remote.model.UserDto
import com.luisptapia.pickupandroid.data.remote.model.UserLoginDto
import com.luisptapia.pickupandroid.data.remote.model.msgDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PickupApi {

    @POST("auth/login")
    suspend fun login(
        @Body body: UserLoginDto
    ): UserDto


    @GET("orders")
    suspend fun getOrders(

        @Query("query")
        query: String?,

        @Query("status")
        status: String?

    ): List<OrderDto>


    @GET("orders/{id}")
    suspend fun getOrderbyId(
        @Path("id")
        id: Int,



    ): OrderDto


    @PATCH("orders/serialnumber/{id}")
    suspend fun updateSerialNumbers(
        @Path("id")
        id: Int,

        @Body body: NumSeriePayload

    ): msgDto

    @PATCH("orders/{id}/status")
    suspend fun updateOrderStatus(
        @Path("id")
        id: Int,

        @Body body: StatusUpdateRequest


    ): OrderDto

}

