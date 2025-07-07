package com.luisptapia.pickupandroid.data

import com.luisptapia.pickupandroid.data.remote.PickupApi
import com.luisptapia.pickupandroid.data.remote.model.NumSeriePayload
import com.luisptapia.pickupandroid.data.remote.model.OrderDto
import com.luisptapia.pickupandroid.data.remote.model.StatusUpdateRequest
import com.luisptapia.pickupandroid.data.remote.model.UserDto
import com.luisptapia.pickupandroid.data.remote.model.UserLoginDto
import com.luisptapia.pickupandroid.data.remote.model.msgDto
import retrofit2.Retrofit

class PickupRepository(
    private val retrofit: Retrofit
) {


    private val pickupApi = retrofit.create(PickupApi::class.java)

    suspend fun login(credentials : UserLoginDto):UserDto = pickupApi.login(credentials)

    suspend fun getOrders(query:String , status : String ):List<OrderDto> = pickupApi.getOrders(query,status)

    suspend fun getOrderbyId(id:Int):OrderDto = pickupApi.getOrderbyId(id)

    suspend fun  updateOrderStatus(orderId: Int , updatePayload : StatusUpdateRequest ) : OrderDto = pickupApi.updateOrderStatus(orderId, updatePayload)

    suspend fun  updateSerialNumber(orderId: Int , numSerie : NumSeriePayload ) : msgDto = pickupApi.updateSerialNumbers(orderId, numSerie)

}