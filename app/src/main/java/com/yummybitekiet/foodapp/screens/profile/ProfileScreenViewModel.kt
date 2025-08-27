package com.yummybitekiet.foodapp.screens.profile

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yummybitekiet.foodapp.Order
import com.yummybitekiet.foodapp.model.User
import com.yummybitekiet.foodapp.network.FirebaseManager
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(private val firebaseManager: FirebaseManager) : ViewModel() {
    private val _userDetails = MutableStateFlow<Map<String, Any>?>(null)
    val userDetails: StateFlow<Map<String, Any>?> get() = _userDetails
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading
    val userid=Firebase.auth.currentUser?.uid
    // Function to fetch user details
    fun getUserDetails() {
        viewModelScope.launch {
            firebaseManager.getUserDetails { userDetails ->
                _userDetails.value = userDetails
            }
        }
    }
    private val _deliveredOrders = MutableStateFlow<List<Order>>(emptyList())
    val deliveredOrders: StateFlow<List<Order>> = _deliveredOrders
    fun fetchDeliveredOrders() {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                firebaseManager.getAllDeliveredOrders()
                    .collect { orders ->
                        _deliveredOrders.value = orders.filter { it.userId== userid }

                        _isLoading.value = false
                    }

            } catch (e: Exception) {
                Log.e("FetchDeliveredOrders", "Error fetching orders", e)

                _isLoading.value = false
            }
        }
    }

}