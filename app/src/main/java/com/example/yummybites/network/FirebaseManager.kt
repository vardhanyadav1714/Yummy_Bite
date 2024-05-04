package com.example.yummybites.network

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.yummybites.Order
import com.example.yummybites.YummyBitesApplication
import com.example.yummybites.model.Food
import com.example.yummybites.model.User
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.max

@Singleton
class FirebaseManager @Inject constructor() {

    init {
        FirebaseApp.initializeApp(YummyBitesApplication.instance)
        FirebaseFirestore.setLoggingEnabled(true)
    }

    private val firestoreInstance = FirebaseFirestore.getInstance()

    fun getStorageReference(): StorageReference {
        return FirebaseStorage.getInstance().reference.child("adminDishes")
    }

    suspend fun getAllDishes(): List<Food> {
        return try {
            Log.d("Firestore", "Fetching all dishes...")

            val db = FirebaseFirestore.getInstance()

            // Specify the collection path for fetching all dishes
            val allDishesCollection = db.collection("all_dishes_and_orders")
                .document("all_dishes")
                .collection("dishes")

            val result = allDishesCollection.get().await()

            val dishesList = mutableListOf<Food>()

            for (document in result.documents) {
                val food = document.toObject(Food::class.java)
                food?.let {
                    dishesList.add(it)
                }
            }

            Log.d("Firestore", "Fetched ${dishesList.size} dishes successfully.")
            dishesList
        } catch (e: FirebaseFirestoreException) {
            Log.e("Firestore", "Firestore exception while fetching dishes: $e")
            e.printStackTrace()
            emptyList()
        } catch (e: Exception) {
            Log.e("Firestore", "Error fetching all dishes: $e")
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getDishesByVendor(vendor: String): List<Food> {
        return try {
            val result = firestoreInstance
                .collection("dishes")
                .document(vendor)
                .collection("user_dishes")
                .get()
                .await()

            Log.d("Firestore", "Result size for $vendor: ${result.size()}")

            result.toObjects(Food::class.java)
        } catch (e: Exception) {
            Log.e("Firestore", "Error fetching dishes by vendor ($vendor): $e")
            e.printStackTrace() // Print stack trace for more details
            emptyList()
        }
    }
    suspend fun saveUserInformation(
        email: String,
        username: String,
        phone: String,
        callback: (Boolean) -> Unit
    ) {
        try {
            val user = Firebase.auth.currentUser
            if (user != null) {
                // Save user information to Firestore
                firestoreInstance.collection("users").document(user.uid).collection("userDetails")
                    .document("userInfo") // Use a specific document ID if needed
                    .set(
                        mapOf(
                            "email" to email,
                            "username" to username,
                            "phone" to phone
                            // Add other fields as needed
                        )
                    ).await()

                callback(true) // Callback indicating success
            } else {
                callback(false) // Callback indicating failure (user is not authenticated)
            }
        } catch (e: Exception) {
            Log.e("Firestore", "Error saving user information: $e")
            e.printStackTrace()
            callback(false) // Callback indicating failure (exception occurred)
        }
    }



    suspend fun addToCart(food: Food) {
        val user = Firebase.auth.currentUser
        if (user != null) {
            try {
                // Update the 'addedToCart' property and add it to the user's cart collection
                firestoreInstance
                    .collection("users")
                    .document(user.uid)
                    .collection("cart_items")
                    .document(food.name)  // Use the food name as the document ID
                    .set(food.copy(addedToCart = true))  // Use set instead of add to specify the document ID
                    .await()
            } catch (e: Exception) {
                Log.e("Firestore", "Error adding to cart: $e")
                e.printStackTrace()
            }
        }
    }

    suspend fun getCartItems(): List<Food> {
        val user = Firebase.auth.currentUser
        return if (user != null) {
            try {
                val result = firestoreInstance
                    .collection("users")
                    .document(user.uid)
                    .collection("cart_items")
                    .get()
                    .await()

                result.toObjects(Food::class.java)
            } catch (e: Exception) {
                Log.e("Firestore", "Error fetching cart items: $e")
                e.printStackTrace()
                emptyList()  // Return an empty list in case of an exception
            }
        } else {
            emptyList()  // Return an empty list if the user is not authenticated
        }
    }

    suspend fun increaseQuantity(food: Food) {
        val user = Firebase.auth.currentUser
        if (user != null) {
            try {
                val cartItemRef = firestoreInstance
                    .collection("users")
                    .document(user.uid)
                    .collection("cart_items")
                    .document(food.name)

                firestoreInstance.runTransaction { transaction ->
                    val snapshot = transaction.get(cartItemRef)
                    val currentQuantity = snapshot.getLong("quantity") ?: 0

                    val newQuantity = currentQuantity + 1
                    val newTotalPrice = food.price.toDouble() * newQuantity

                    transaction.update(cartItemRef, "quantity", newQuantity, "totalPrice", newTotalPrice)
                }.await()
            } catch (e: Exception) {
                Log.e("Firestore", "Error in increaseQuantity: $e")
                e.printStackTrace()
            }
        }
    }
    suspend fun saveOrderForUser(order: Order) {
        val user = Firebase.auth.currentUser
        if (user != null) {
            try {
                // Save order to the user's orders collection
                firestoreInstance
                    .collection("users")
                    .document(user.uid)
                    .collection("orders")
                    .add(order)
                    .await()
            } catch (e: Exception) {
                Log.e("Firestore", "Error saving order for user: $e")
                e.printStackTrace()
            }
        }
    }

    suspend fun getOrderForUser(): List<Order> {
        return try {
            val user = Firebase.auth.currentUser
            if (user != null) {
                val db = FirebaseFirestore.getInstance()
                val allOrderCollection = db.collection("users").document(user.uid).collection("orders")
                val result = allOrderCollection.get().await()
                val ordersList = mutableListOf<Order>()

                for (document in result.documents) {
                    val order = document.toObject(Order::class.java)
                    order?.let {
                        ordersList.add(it)
                    }
                }

                Log.d("Firestore", "Fetched ${ordersList.size} orders successfully.")
                ordersList
            } else {
                emptyList()
            }
        } catch (e: FirebaseFirestoreException) {
            Log.e("Firestore", "Firestore exception while fetching orders: $e")
            e.printStackTrace()
            emptyList()
        } catch (e: Exception) {
            Log.e("Firestore", "Error fetching all orders: $e")
            e.printStackTrace()
            emptyList()
        }
    }


    suspend fun saveOrderForAll(order: Order) {
        try {
            // Save order to the collection for all users
            firestoreInstance.collection("all_dishes_and_orders")
                .document("all_dishes")
                .collection("orders")
                .add(order)
                .await()
        } catch (e: Exception) {
            Log.e("Firestore", "Error saving order for all users: $e")
            e.printStackTrace()
        }
    }
    suspend fun getAllDeliveredOrders(): Flow<List<Order>> = flow {
            val userId = Firebase.auth.currentUser?.uid
            if (userId != null) {
                val db = FirebaseFirestore.getInstance()
                val deliveredOrdersCollection = db.collection("all_dishes_and_orders")
                    .document("all_dishes")
                    .collection("deliveredOrders")
                Log.d("Firestore","You are inside the firebase collection")
                try {
                    val querySnapshot=deliveredOrdersCollection.get().await()
                    val orderList = mutableListOf<Order>()
                    for (doc in querySnapshot.documents) {
                        val order = doc.toObject<Order>()
                        if (order != null) {
                            orderList.add(order)
                            Log.d("Firestore","Order has ")

                        }
                    }
                    emit(orderList)
                } catch (e: Exception) {
                    Log.e("Firestore", "Error getting delivered orders", e)
                    // Handle error
                }
            }
    }



    suspend fun getAllOrders(): List<Order> {
        val user=Firebase.auth.currentUser
        return try {
            Log.d("Firestore", "Fetching all orders...")

            val db = FirebaseFirestore.getInstance()

            val allOrdersCollection = db.collection("all_dishes_and_orders")
                .document("all_dishes")
                .collection("orders").get().await()


            val ordersList = mutableListOf<Order>()

            for (document in allOrdersCollection.documents) {
                val order = document.toObject(Order::class.java)
                order?.let {
                    if(it.userId == user?.uid) {
                        ordersList.add(it)
                    }
                }
            }

            Log.d("Firestore", "Fetched ${ordersList.size} orders successfully.")
            ordersList
        } catch (e: FirebaseFirestoreException) {
            Log.e("Firestore", "Firestore exception while fetching orders: $e")
            e.printStackTrace()
            emptyList()
        } catch (e: Exception) {
            Log.e("Firestore", "Error fetching all orders: $e")
            e.printStackTrace()
            emptyList()
        }
    }

    //    suspend fun getUserDetails(callback: (User?) -> Unit) {
//        try {
//            val user = Firebase.auth.currentUser
//            if (user != null) {
//                // Fetch user details from Firestore
//                val documentSnapshot = firestoreInstance
//                    .collection("users")
//                    .document(user.uid)
//                    .collection("userDetails")
//                    .document("userInfo") // Use a specific document ID if needed
//                    .get()
//                    .await()
//
//                if (documentSnapshot.exists()) {
//                    val data = documentSnapshot.data
//                    if (data != null) {
//                        val email = data["email"] as? String ?: ""
//                        val username = data["username"] as? String ?: ""
//                        val phone = data["phone"] as? String ?: ""
//
//                        val userDetails = User(email, username, phone)
//                        Log.d("Firestore", "UserDetails retrieved: $userDetails")
//                        callback(userDetails)
//                    } else {
//                        Log.e("Firestore", "Data is null")
//                        callback(null)
//                    }
//                } else {
//                    Log.d("Firestore", "No user details found")
//                    callback(null) // No user details found
//                }
//            } else {
//                Log.d("Firestore", "User is not authenticated")
//                callback(null) // User is not authenticated
//            }
//        } catch (e: Exception) {
//            Log.e("Firestore", "Error fetching user details: $e")
//            e.printStackTrace()
//            callback(null) // Error occurred while fetching user details
//        }
//    }
suspend fun getUserDetails(callback: (Map<String, Any>?) -> Unit) {
    try {
        val user = Firebase.auth.currentUser
        if (user != null) {
            // Fetch user details from Firestore
            val documentSnapshot = firestoreInstance
                .collection("users")
                .document(user.uid)
                .collection("userDetails")
                .document("userInfo") // Use a specific document ID if needed
                .get()
                .await()

            if (documentSnapshot.exists()) {
                val userDetails = documentSnapshot.data
                callback(userDetails)
            } else {
                callback(null) // No user details found
            }
        } else {
            callback(null) // User is not authenticated
        }
    } catch (e: Exception) {
        Log.e("Firestore", "Error fetching user details: $e")
        e.printStackTrace()
        callback(null) // Error occurred while fetching user details
    }
}

    suspend fun decreaseQuantity(food: Food) {
        val user = Firebase.auth.currentUser
        if (user != null) {
            try {
                val cartItemRef = firestoreInstance
                    .collection("users")
                    .document(user.uid)
                    .collection("cart_items")
                    .document(food.name)

                firestoreInstance.runTransaction { transaction ->
                    val snapshot = transaction.get(cartItemRef)
                    val currentQuantity = snapshot.getLong("quantity") ?: 0

                    val newQuantity = max(0, currentQuantity - 1)
                    val newTotalPrice = food.price.toDouble() * newQuantity

                    if (newQuantity > 0) {
                        transaction.update(cartItemRef, "quantity", newQuantity, "totalPrice", newTotalPrice)
                    } else {
                        transaction.delete(cartItemRef)
                    }
                }.await()
            } catch (e: Exception) {
                Log.e("Firestore", "Error in decreaseQuantity: $e")
                e.printStackTrace()
            }
        }
    }


    suspend fun removeCartItem(food: Food) {
        val user = Firebase.auth.currentUser
        if (user != null) {
            try {
                firestoreInstance
                    .collection("users")
                    .document(user.uid)
                    .collection("cart_items")
                    .document(food.name)
                    .delete()
                    .await()
            } catch (e: Exception) {
                Log.e("Firestore", "Error in removeCartItem: $e")
                e.printStackTrace()
            }
        }
    }

    suspend fun clearCart() {
        val user = Firebase.auth.currentUser
        if (user != null) {
            try {
                // Delete all documents in the user's cart collection
                firestoreInstance
                    .collection("users")
                    .document(user.uid)
                    .collection("cart_items")
                    .get()
                    .await()
                    .documents
                    .forEach { document ->
                        document.reference.delete().await()
                    }
            } catch (e: Exception) {
                Log.e("Firestore", "Error clearing cart: $e")
                e.printStackTrace()
            }
        }
    }



    private fun getCartItemReference(userId: String, food: Food) =
        firestoreInstance.collection("users").document(userId).collection("cart").document(food.name)

}
