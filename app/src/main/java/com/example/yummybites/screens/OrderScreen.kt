package com.example.yummybites.screens

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.yummybites.Order
import com.example.yummybites.OrderPayment
import com.example.yummybites.OrderStatus
import com.example.yummybites.OrderTpe
import com.example.yummybites.R
import com.example.yummybites.screens.home.DishViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

private const val CHANNEL_ID = "order_status_channel"
private const val NOTIFICATION_ID_BASE = 1000 // Unique base ID for notifications

@Composable
fun OrderScreen(viewModel: DishViewModel = hiltViewModel()) {
    val context = LocalContext.current
    // Fetch cart items when the composable is first displayed
    LaunchedEffect(key1 = true) {
        viewModel.fetchAllOrders()
        observeOrderStatus(context, viewModel)
    }

    // Collect cart items from the ViewModel
    val userOrders by viewModel.allorders.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        backgroundColor = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (isLoading) {
                // Display circular progress indicator when loading
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                if (userOrders.isNotEmpty()) {
                    Text(
                        text = "Your Orders",
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    LazyColumn {
                        items(userOrders) { order ->
                            OrderItem(order = order)
                            Spacer(modifier = Modifier.height(16.dp)) // Add space between orders
                        }
                    }
                } else {
                    Text(
                        text = "No orders found",
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun OrderItem(order: Order) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = 8.dp,
        backgroundColor = MaterialTheme.colors.surface,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Order items (food name, quantity, price)
            order.foodList.forEach { food ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.Start
                ) {
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

            // Display vendor name
            Text(
                text = "Vendor: ${order.vendorName}",
                style = MaterialTheme.typography.body2,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colors.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Display order type
            Text(
                text = "Order Type: ${order.orderType.name}",
                style = MaterialTheme.typography.body2,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colors.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Display order payment status
            val paymentColor = if (order.orderPayment == OrderPayment.Paid) Color.Green else Color.Red
            Text(
                text = "Payment Status: ${order.orderPayment.name}",
                style = MaterialTheme.typography.body2,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = paymentColor
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Display order status
            Text(
                text = "Order Status: ${order.orderStatus.name}",
                style = MaterialTheme.typography.body2,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colors.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Display total amount
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

private suspend fun observeOrderStatus(context: Context, viewModel: DishViewModel) {
    viewModel.allorders.filter { it.isNotEmpty() }
        .distinctUntilChanged()
        .collect { order ->
            order.forEach {
                if (it.orderStatus != OrderStatus.Preparing) {
                    showNotification(context, it)
                }
            }
        }
}

fun showNotification(context: Context, order: Order) {
    createNotificationChannel(context)
    val contentText = when (order.orderStatus) {
        OrderStatus.Ready -> {
            val foodNames = order.foodList.joinToString { it.name }
            "Your Order for $foodNames is Ready, Kindly collect it from the outlet"
        }
        OrderStatus.Preparing -> {
            val foodNames = order.foodList.joinToString { it.name }
            "Your order for $foodNames is getting Prepared, we will notify you when it will be ready"
        }
        OrderStatus.Delivered -> {
            if (order.orderPayment == OrderPayment.Paid) {
                val foodNames = order.foodList.joinToString { it.name }
                "Your Order for $foodNames was delivered Successfully, thank you for ordering"
            } else {
                "Your Order was delivered, but payment is pending"
            }
        }
    }

    // Generate a unique notification ID based on order.orderId
    val notificationId = order.orderId.hashCode()

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_background)
        .setContentTitle("Your Order Status")
        .setContentText(contentText)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    with(NotificationManagerCompat.from(context)) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context)
        }
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Handle permission request
            return
        }
        notify(notificationId, builder.build())
    }
}

fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            val name = "Order status"
            val descriptionText = "Channel for order status"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            notificationManager.createNotificationChannel(channel)
        }
    }
}
