package com.yummybitekiet.foodapp.screens.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.RemoveShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.yummybitekiet.foodapp.model.Food
import com.yummybitekiet.foodapp.navigation.YummyBitesScreens
import com.yummybitekiet.foodapp.screens.home.DishViewModel
import com.yummybitekiet.foodapp.ui.theme.Shapes

@Composable
fun CartScreen(navController: NavController, cartViewModel: DishViewModel = hiltViewModel()) {
    // rest of your code

    val cartItems by cartViewModel.cartitemlist.collectAsState(emptyList())
    val loading by cartViewModel.loading.collectAsState(false)

    // Use LaunchedEffect to trigger the data loading effect
    LaunchedEffect(key1 = cartViewModel) {
        cartViewModel.getCartItems()
    }

    // Calculate total amount
    val totalAmount = cartItems.sumBy { it.totalPrice.toInt() }
   if (cartItems.isEmpty()){
       Column(
           modifier = Modifier
               .fillMaxSize()
               .padding(16.dp),
           verticalArrangement = Arrangement.Top
       ) {
           Text(text = "No Item in cart")
       }
   }
    else {
       Column(
           modifier = Modifier
               .fillMaxSize()
               .padding(16.dp),
           verticalArrangement = Arrangement.Top
       ) {
           if (loading) {
               CircularProgressIndicator()
           } else {


               LazyColumn(
                   modifier = Modifier
                       .fillMaxWidth()

                       .weight(1f)
               ) {
                   itemsIndexed(cartItems) { index, food ->
                       CartItem(food = food, onRemoveItemClick = {
                           cartViewModel.removeFromCart(food)
                       }, onIncreaseClick = {
                           cartViewModel.increaseQuantity(food)
                       }, onDecreaseClick = {
                           cartViewModel.decreaseQuantity(food)
                       })
                       Spacer(modifier = Modifier.height(8.dp))
                   }
               }

               // Display total amount
               Text(
                   text = "Total Amount: ₹${totalAmount}",
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(bottom = 8.dp),
                   fontWeight = FontWeight.Bold,
                   fontSize = 18.sp,
                   color = MaterialTheme.colors.onSurface
               )

               // Spacer to push total amount and buttons to the bottom
               Spacer(modifier = Modifier.weight(1f))

               // Buttons
               Row(
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(8.dp),
                   horizontalArrangement = Arrangement.SpaceBetween
               ) {
                   if (totalAmount > 0) {
                       Button(onClick = {
                           cartViewModel.clearCart()
                           cartViewModel.getCartItems()
                       }) {
                           Text("Cancel")
                       }
                   }
                   if (totalAmount > 0) {
                       Button(onClick = { navController.navigate("${YummyBitesScreens.PaymentScreen}/$totalAmount") }) {
                           Text("Proceed to Checkout")
                       }
                   }
               }
           }
       }
   }
}


@Composable
fun CartItem(
    food: Food,
    onRemoveItemClick: () -> Unit,
    onIncreaseClick: () -> Unit,
    onDecreaseClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(Shapes.medium),
        shape = RoundedCornerShape(corner = CornerSize(12.dp)),

        elevation = 4.dp,
     ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Food details
            Column {
                Text(
                    text = food.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colors.onSurface
                )
                Text(
                    text = "Price: ₹${food.price}",
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.secondary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Quantity control
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Decrease Quantity Button
                RoundIconButton(
                    onClick = onDecreaseClick,
                    icon = Icons.Default.RemoveShoppingCart,
                    iconColor = Color.White,
                    backgroundColor = Color.Red
                )

                // Display Quantity
                Text(
                    text = food.quantity.toString(),
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = MaterialTheme.colors.onSurface
                )

                // Increase Quantity Button
                RoundIconButton(
                    onClick = onIncreaseClick,
                    icon = Icons.Default.AddShoppingCart,
                    iconColor = Color.White,
                    backgroundColor = Color.Green
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Total price
            Text(
                text = "Total: ₹${food.totalPrice}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        // Remove item in top-right corner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            RoundIconButton(
                onClick = onRemoveItemClick,
                icon = Icons.Default.Delete,
                iconColor = Color.White,
                backgroundColor = Color.Gray
            )
        }
    }
}

@Composable
fun RoundIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    iconColor: Color,
    backgroundColor: Color
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(24.dp) // Adjust the size as needed
            .background(backgroundColor, CircleShape)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Icon",
            tint = iconColor
        )
    }
}
