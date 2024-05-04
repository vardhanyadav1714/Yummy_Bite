package com.example.yummybites.screens.profile


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*

import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.yummybites.Order
import com.example.yummybites.R
import com.example.yummybites.model.User
import com.example.yummybites.navigation.YummyBitesScreens

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileScreenViewModel = hiltViewModel()) {
    var user by remember { mutableStateOf(User("", "", "")) }
    val previousOrders by viewModel.deliveredOrders.collectAsState(emptyList())
    val isLoading by viewModel.isLoading.collectAsState()
    LaunchedEffect(true){
        viewModel.fetchDeliveredOrders()
    }
    LaunchedEffect(Unit) {
        viewModel.getUserDetails()
        viewModel.userDetails.collect { userDetails ->
            if (userDetails != null) {
                // Extract values directly from userDetails Map
                val name = userDetails["username"] as? String ?: ""
                val email = userDetails["email"] as? String ?: ""
                val phone = userDetails["phone"] as? String ?: ""

                // Create a User object if needed
                user = User(name, email, phone)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Profile Image
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(MaterialTheme.shapes.medium)
                .align(Alignment.CenterHorizontally)
        ) {
            // You can load the image from the drawable or any URL
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // User Details
        LazyColumn {
            item {
                // Check if userDetails is not null before accessing its properties
                ProfileItem("Email", user.username)
                ProfileItem("UserName", user.email)
                ProfileItem("Phone", user.phone)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Logout Button
        Button(
            onClick = {
                FirebaseAuth.getInstance().signOut()
                navController.navigate(YummyBitesScreens.LoginScreen.name) // Navigate to your login screen
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(23.dp)
        ) {
            Text(text = "Sign Out")
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Previous Orders",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp), style = TextStyle(fontWeight= FontWeight.Bold, fontSize = 18.sp)

        )
        if (isLoading) {
            // Display circular progress indicator while loading
            CircularProgressIndicator(
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.CenterHorizontally)
            )
        } else if (previousOrders.isNotEmpty()) {
            LazyColumn {
                items(previousOrders) {
                    OrderCard(it)
                }
            }
        } else {
            // Display a message when there are no delivered orders
            Text(
                text = "No previous orders found",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp)
            )
        }
    }
}

@Composable
fun ProfileItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colors.primary
            )
        )

        Text(
            text = value,
            style = TextStyle(
                fontSize = 16.sp,
                color = Color.Gray
            )
        )
    }
}

@Composable
fun OrderCard(order: Order) {
    Card(
        modifier = Modifier
            .fillMaxWidth().padding(5.dp),
        elevation = 8.dp,
        backgroundColor = MaterialTheme.colors.background,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            order.foodList.forEach { food ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()

                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Ordered from :${order.vendorName}",
                        style = MaterialTheme.typography.subtitle2,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${food.name} x ${food.quantity}",
                            style = MaterialTheme.typography.subtitle1,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.onSurface
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "₹${"%.2f".format(food.totalPrice)}",
                            style = MaterialTheme.typography.body2,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.secondary
                        )
                    }

                }
            }

            Spacer(modifier = Modifier.height(8.dp))


            Text(
                text = "Total Amount: ₹${"%.2f".format(order.totalAmount)}",
                style = MaterialTheme.typography.h6,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.secondary
            )
        }
    }
}