package com.yummybitekiet.foodapp.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yummybitekiet.foodapp.Order
import com.yummybitekiet.foodapp.model.Food
import com.yummybitekiet.foodapp.network.FirebaseManager
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

@HiltViewModel
class DishViewModel @Inject constructor(private val firebaseManager: FirebaseManager) : ViewModel() {

    private val _foodList = MutableStateFlow<List<Food>>(emptyList())
    val foodList: StateFlow<List<Food>> = _foodList
    var isFetching =  false
    private val _allDishes = MutableStateFlow<List<Food>>(emptyList())
    val allDishes: StateFlow<List<Food>> = _allDishes
    private val _cartAdditionStatus = MutableLiveData<Boolean>()
    val cartAdditionStatus: LiveData<Boolean> = _cartAdditionStatus
    // Function to fetch all dishes from Firebase

    fun fetchAllDishes() {
        viewModelScope.launch {
            try {
                val dishes = firebaseManager.getAllDishes()
                _allDishes.value = dishes
                _foodList.value = dishes // Update _foodList as well
                isFetching = true
            } catch (e: Exception) {
                // Log or handle the exception
                e.printStackTrace()
            } finally {
                isFetching = false
            }
        }
    }

    private val _allorders = MutableStateFlow<List<Order>>(emptyList())
    val allorders:StateFlow<List<Order>> = _allorders
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    fun fetchAllOrders(){
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val order = firebaseManager.getAllOrders()
                _allorders.value= order
            }catch (e: Exception) {
                // Log or handle the exception
                e.printStackTrace()
            } finally {
                _isLoading.value = false

            }
        }
    }
    private val _alluserorders = MutableStateFlow<List<Order>>(emptyList())
    val alluserorders:StateFlow<List<Order>> = _allorders

    fun fetchAllUserOrders() {
        viewModelScope.launch {
            try {
                val orders = firebaseManager.getOrderForUser()
                _alluserorders.value = orders
            } catch (e: Exception) {
                // Handle error or log exception
                e.printStackTrace()
            }
        }
    }
    fun fetchDishesByVendor(vendor: String) {
        viewModelScope.launch {
            try {
                val dishes = firebaseManager.getDishesByVendor(vendor)
                _foodList.value = dishes
            } catch (e: Exception) {
                // Handle error if needed
                e.printStackTrace()
            }
        }
    }
    private val _selectedFood = MutableLiveData<Food?>()
    val selectedFood: LiveData<Food?> = _selectedFood

    fun onAddToCartClick(food: Food) {
        viewModelScope.launch {
            firebaseManager.addToCart(food)
            _selectedFood.value = food
            _cartAdditionStatus.value = true
         }
        _cartAdditionStatus.value = true
    }

    fun clearSelectedFood() {
        _selectedFood.value = null
    }
    private val _cartItemList = MutableStateFlow<List<Food>>(emptyList())
    val cartitemlist: StateFlow<List<Food>> = _cartItemList

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading
    fun getCartItems() {
        viewModelScope.launch {
            try {
                _loading.value=true
                val items = firebaseManager.getCartItems()
                _cartItemList.value = items
            } catch (e: Exception) {
                e.printStackTrace()

            }finally {
                _loading.value = false
            }

        }
    }
   private val _filteredDishes = MutableStateFlow<List<Food>>(emptyList())
    val filteredDishes: StateFlow<List<Food>> = _filteredDishes

    fun filterDishes(
         selectedVendor: String,
        minPrice: Float,
        maxPrice: Float
    ) {
        // Filter dishes based on the provided criteria
        val filteredList = foodList.value.filter { food ->
             val isVendorMatch = selectedVendor.isBlank() || food.vendor == selectedVendor
            val isPriceInRange = food.price.toFloat() in minPrice..maxPrice
             isVendorMatch && isPriceInRange
        }
        _filteredDishes.value = filteredList
    }
    private val _searchedDishes = MutableStateFlow<List<Food>>(emptyList())
    val searchedDishes: StateFlow<List<Food>> = _searchedDishes

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(300) // Adjust debounce time as needed (milliseconds)
                .distinctUntilChanged()
                .collect { query ->
                    searchDishes(query)
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun searchDishes(searchText: String) {
        val filteredList = _foodList.value.filter { food ->
            food.name.contains(searchText, ignoreCase = true)
        }
        _searchedDishes.value = filteredList
    }

    fun increaseQuantity(food: Food) {
        viewModelScope.launch {
            firebaseManager.increaseQuantity(food)
            getCartItems()  // Refresh the cart items after modification
        }
    }
    fun clearCart() {
        viewModelScope.launch {
            firebaseManager.clearCart()
        }
    }

    fun decreaseQuantity(food: Food) {
        viewModelScope.launch {
            firebaseManager.decreaseQuantity(food)
            getCartItems()  // Refresh the cart items after modification
        }
    }
    fun removeFromCart(food: Food) {
        viewModelScope.launch {
            firebaseManager.removeCartItem(food)
            getCartItems()  // Refresh the cart items after removal
        }
    }
    fun getCurrentUserId(): String {
        val user = Firebase.auth.currentUser
        return user?.uid ?: ""
    }

    fun saveOrderForUser(order: Order) {
        viewModelScope.launch {
            firebaseManager.saveOrderForUser(order)
        }
    }

    fun saveOrderForAll(order: Order) {
        viewModelScope.launch {
            firebaseManager.saveOrderForAll(order)
        }
    }
}
