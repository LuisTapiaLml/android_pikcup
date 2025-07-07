package com.luisptapia.pickupandroid.modelView

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luisptapia.pickupandroid.data.PickupRepository
import com.luisptapia.pickupandroid.data.remote.model.NumSeriePayload
import com.luisptapia.pickupandroid.data.remote.model.OrderDto
import com.luisptapia.pickupandroid.data.remote.model.StatusUpdateRequest
import kotlinx.coroutines.launch

class OrderDetailViewModel(
    private val repository: PickupRepository
):ViewModel() {

    private val _order = MutableLiveData<OrderDto?>()
    val order: LiveData<OrderDto?> = _order

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _statusText = MutableLiveData<String>()
    val statusText: LiveData<String> = _statusText


    fun loadOrder(orderId: Int) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val orderResult = repository.getOrderbyId(orderId)
                _order.value = orderResult
                _statusText.value = mapStatusText(orderResult.status)
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar pedido: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateOrderStatus(orderId: Int, status: String){
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val orderResult = repository.updateOrderStatus(orderId, StatusUpdateRequest(status))
                _order.value = orderResult
                _statusText.value = mapStatusText(orderResult.status)

            } catch (e: Exception) {
                _errorMessage.value = "Error al actualizar estatus: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }



    fun updateNumSerie(orderId: Int, payload: NumSeriePayload){
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val orderResult = repository.updateSerialNumber(orderId, payload)

            } catch (e: Exception) {
                _errorMessage.value = "Error al actualizar estatus: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }



    fun refresh(orderId: Int) {
        loadOrder(orderId)
    }

    private fun mapStatusText(status: String): String {
        return when (status.lowercase()) {
            "asignado" -> "Confirmación de Pedido"
            "confirmado" -> "Entregar Pedido"
            "cancelado" -> "Pedido Cancelado"
            "entregado" -> "Pedido Entregado"
            else -> ""
        }
    }


}