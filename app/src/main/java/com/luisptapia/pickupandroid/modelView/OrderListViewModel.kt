package com.luisptapia.pickupandroid.modelView

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luisptapia.pickupandroid.data.PickupRepository
import com.luisptapia.pickupandroid.data.remote.model.OrderDto
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class OrderListViewModel(
    private val repository: PickupRepository
) : ViewModel() {

    private val _orders = MutableLiveData<List<OrderDto>>()
    val orders: LiveData<List<OrderDto>> get() = _orders

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private var debounceJob: Job? = null

    private var currentQuery: String = ""
    private var currentStatus: String = "todos"

    fun setQuery(query: String) {
        if (currentQuery != query) {
            currentQuery = query
            debounceLoadOrders()
        }
    }

    fun setStatusFilter(status: String) {
        if (currentStatus != status) {
            currentStatus = status
            debounceLoadOrders()
        }
    }

    private fun debounceLoadOrders() {
        debounceJob?.cancel()
        debounceJob = viewModelScope.launch {
            delay(500)
            _isLoading.value = true
            loadOrders(currentQuery, currentStatus)
        }
    }


    fun loadOrders(query: String = "", status: String = "todos") {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val result = repository.getOrders(query, status)
                _orders.postValue(result)
            } catch (e: Exception) {
                _errorMessage.postValue(e.message ?: "Error desconocido")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun refresh() {
        loadOrders()
    }
}
