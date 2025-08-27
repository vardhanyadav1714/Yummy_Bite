package com.yummybitekiet.foodapp.model

import com.google.firebase.firestore.PropertyName

data class Food(
    var id: String = "",
    @get:PropertyName("name")
    @set:PropertyName("name")
    var name: String = "",
    @get:PropertyName("price")
    @set:PropertyName("price")
    var price: String = "",
    @get:PropertyName("imageUrl")
    @set:PropertyName("imageUrl")
    var imageUrl: String = "",
    @get:PropertyName("availability")
    @set:PropertyName("availability")
    var availability: Boolean = false,
    @get:PropertyName("vendor")
    @set:PropertyName("vendor")
    var vendor: String = "",
    @get:PropertyName("addedToCart")
    @set:PropertyName("addedToCart")
    var addedToCart: Boolean = false,
    @get:PropertyName("quantity")
    @set:PropertyName("quantity")
    var quantity: Int = 1,
    @get:PropertyName("vendorUid")
    @set:PropertyName("vendorUid")
    var vendorUid:String=""

) {
    // Custom getter for total calculated price
    val totalPrice: Double
        get() = calculateTotalPrice()

    // Function to calculate total price
    fun calculateTotalPrice(): Double {
        return price.toDouble() * quantity
    }
}
