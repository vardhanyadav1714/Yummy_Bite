package com.yummybitekiet.foodapp.screens.payments
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.yummybitekiet.foodapp.*
import com.yummybitekiet.foodapp.model.Food
import com.yummybitekiet.foodapp.model.User
import com.yummybitekiet.foodapp.navigation.YummyBitesScreens
import com.yummybitekiet.foodapp.screens.home.DishViewModel
import com.yummybitekiet.foodapp.screens.profile.ProfileScreenViewModel
import com.razorpay.Checkout
import dev.shreyaspatil.easyupipayment.EasyUpiPayment
import dev.shreyaspatil.easyupipayment.listener.PaymentStatusListener
import dev.shreyaspatil.easyupipayment.model.PaymentApp
import dev.shreyaspatil.easyupipayment.model.TransactionDetails
import dev.shreyaspatil.easyupipayment.model.TransactionStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PaymentsScreen(
    navController: NavHostController,
    viewModel: DishViewModel,
    totallAmount: Double,
    profileviewmodel:ProfileScreenViewModel= hiltViewModel()
     //cartItems: List<Food>
)   {
    var isLoading by remember { mutableStateOf(false) }
    val GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user"
    val GOOGLE_PAY_REQUEST_CODE = 123
    val scope = rememberCoroutineScope()


    val orderType by remember {
        mutableStateOf(OrderTpe.PayAtOutlet)
    }
   var createnow by remember {mutableStateOf(false)}
    val contextt = LocalContext.current
     LaunchedEffect(viewModel) {
        viewModel.getCartItems()
    }
    val cartItems by viewModel.cartitemlist.collectAsState()
    val groupedItems = cartItems.groupBy { it.vendorUid }

    // Determine the number of unique vendors
    val numVendors = groupedItems.size

    val totalAmount = cartItems.sumBy { it.totalPrice.toInt() }
    var user by remember { mutableStateOf(User("", "", "")) }
    val activity = (LocalContext.current as? Activity)
    LaunchedEffect(Unit) {
        profileviewmodel.getUserDetails()
        profileviewmodel.userDetails.collect { userDetails ->
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

    val order = Order(
        orderId= UUID.randomUUID().toString(),
        userId = viewModel.getCurrentUserId(),
        userName = user.email,
        vendorUserId = cartItems.firstOrNull()?.vendorUid ?: "",
        vendorName = cartItems.firstOrNull()?.vendor ?: "",
        foodList = cartItems, // Use cart items as food list
        totalAmount = totallAmount,
        orderType = orderType, // Payment type determines order type
        orderPayment = if (orderType == OrderTpe.PaidUsingRazorPay) OrderPayment.Paid else OrderPayment.Unpaid,
        orderStatus = OrderStatus.Preparing
    )
    fun createOrders(updatedOrderType: OrderTpe) {
        // Create one order if all items belong to the same vendor
        if (numVendors == 1) {
            val vendorUid = cartItems.first().vendorUid
            val vendorName = cartItems.first().vendor ?: ""
            val orderr = Order(
                orderId = UUID.randomUUID().toString(),
                userId = viewModel.getCurrentUserId(),
                userName = user.email,
                vendorUserId = vendorUid,
                vendorName = vendorName,
                foodList = cartItems,
                totalAmount = totallAmount,
                orderType = updatedOrderType, // Use the updated order type here
                orderPayment = if (updatedOrderType == OrderTpe.PaidUsingRazorPay) OrderPayment.Paid else OrderPayment.Unpaid,
                orderStatus = OrderStatus.Preparing
            )
            scope.launch {
                viewModel.saveOrderForAll(orderr)
                viewModel.clearCart()
            }
        } else {
            // Create individual orders for each vendor
            groupedItems.forEach { (vendorUid, items) ->
                val vendorName = items.firstOrNull()?.vendor ?: ""
                val orderr = Order(
                    orderId = UUID.randomUUID().toString(),
                    userId = viewModel.getCurrentUserId(),
                    userName = user.email,
                    vendorUserId = vendorUid,
                    vendorName = vendorName,
                    foodList = items,
                    totalAmount = items.sumByDouble { it.totalPrice },
                    orderType = updatedOrderType, // Use the updated order type here
                    orderPayment = if (updatedOrderType == OrderTpe.PaidUsingRazorPay) OrderPayment.Paid else OrderPayment.Unpaid,
                    orderStatus = OrderStatus.Preparing
                )
                scope.launch {
                    viewModel.saveOrderForAll(orderr)
                }
            }
            viewModel.clearCart()
        }
    }

    fun createOrderWithUpdatedType(updatedOrderType: OrderTpe) {
        var updatedOrder = order.copy(orderType = updatedOrderType)
        if (updatedOrder.orderType == OrderTpe.PaidUsingRazorPay) {
            updatedOrder = updatedOrder.copy(orderPayment = OrderPayment.Paid)
        }
        if (!createnow) {
            createnow = true
            scope.launch {
                viewModel.saveOrderForAll(updatedOrder)
                viewModel.clearCart()
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {


        // Payment details
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = 8.dp,
            backgroundColor = MaterialTheme.colors.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Payment Details",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = MaterialTheme.colors.primary
                )
                Text(
                    text = "Total Amount: ₹${"%.2f".format(totallAmount)}",
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
             }
        }

         Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = 8.dp,
            backgroundColor = MaterialTheme.colors.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Cart Items",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = MaterialTheme.colors.primary
                )
                cartItems.forEach { food ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        Text(
                            text = "${food.name} x ${food.quantity}",
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "₹${"%.2f".format(food.totalPrice)}",
                            style = MaterialTheme.typography.body1
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                // Total price
                Text(
                    text = "Total: ₹${"%.2f".format(totallAmount)}",
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }

        // Email Input
//        OutlinedTextField(
//            value = email,
//            onValueChange = { email = it },
//            label = { Text("Email") },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 16.dp),
//            keyboardOptions = KeyboardOptions.Default.copy(
//                keyboardType = KeyboardType.Email,
//                imeAction = ImeAction.Done
//            ),
//            keyboardActions = KeyboardActions(
//                onDone = {
//                    // Hide keyboard on Done
//                    // keyboardController?.hide()
//                }
//            ),
//            colors = TextFieldDefaults.outlinedTextFieldColors(
//                focusedBorderColor = MaterialTheme.colors.primary,
//                unfocusedBorderColor = MaterialTheme.colors.onSurface
//            )
//        )

        // Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                     val updatedorderType=OrderTpe.PayAtOutlet
                   // createOrderWithUpdatedType(updatedorderType)
                    createOrders(updatedorderType)
                   isLoading = true
                    scope.launch {
                         delay(1000)
                        navController.navigate(YummyBitesScreens.OrderScreen.name)

                        navController.navigate(YummyBitesScreens.CartScreen.name)
                        isLoading = false
                        Toast.makeText(contextt, "Your order has been successfully placed", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.secondary,
                    contentColor = MaterialTheme.colors.onSecondary
                )
            ) {
                Text(
                    text = "Pay at Pickup",
                    color = MaterialTheme.colors.onSecondary
                )
            }

            Button(
                onClick = {

                        // Proceed with paymentX
                         val name="Yummy Bite"
                        val upi="eiov688.rzp@axisbank"
//                        val transactionId= generateTransactionId()
//                        val uri: Uri = Uri.Builder()
//                            .authority("pay")
//                            .appendQueryParameter("pa", "stk-9760347653-1@okbizaxis")
//                            .appendQueryParameter("pn", "Yummy Bite")
//                            .appendQueryParameter("mc", "BCR2DN4T67UPN5Q3")
//                            .appendQueryParameter("tr", generateFixedLengthNumericString())
//                            .appendQueryParameter("tn", "paymnet for food")
//                            .appendQueryParameter("am", totallAmount.toString())
//                            .appendQueryParameter("cu", "INR")
//                             .build()
//
//                        val intent = Intent(Intent.ACTION_VIEW).apply {
//                            data = uri
//                            setPackage(GOOGLE_PAY_PACKAGE_NAME)
//                        }
//
//                        if (activity != null) {
//                            activity.startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE)
//                        }
                        val paymentStatusListener = object : PaymentStatusListener {
                            override fun onTransactionCancelled() {
                                Toast.makeText(contextt,"Payment was cancelled",Toast.LENGTH_SHORT).show()
                            }

                            override fun onTransactionCompleted(transactionDetails: TransactionDetails) {
                                if (transactionDetails.transactionStatus==TransactionStatus.SUCCESS) {
                                    // Transaction was successful

                                    val updatedOrderType = OrderTpe.PaidUsingRazorPay
                                    createOrders(updatedOrderType)
                                    createnow=false
                                    scope.launch {
                                        delay(1000)
                                        navController.navigate(YummyBitesScreens.OrderScreen.name)
                                        navController.navigate(YummyBitesScreens.CartScreen.name)
                                    }
                                } else {
                                    // Transaction failed
                                    // You can handle failed transactions here
                                }
                            }
                    }

                    initiateUpiPayment(contextt,totallAmount.toString(),upi,name,"payment", paymentStatusListener)
                       //  initiatePayment(context,email,totallAmount.toString())

                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary
                )
            ) {
                Text(
                    text = "Pay Now",
                    color = MaterialTheme.colors.onPrimary
                )
            }
        }
        if (isLoading) {
            CircularProgressIndicator()
            Text(
                text = "Processing...",
                style = MaterialTheme.typography.body1

            )

        }
    }
}



//fun initiatePayment(
//    context: Context,
//    email: String,
//    amount: String,
//  ) {
//    val roundedAmount = "%.2f".format(amount.toDouble())
//    val checkout = Checkout()
//    checkout.setKeyID("rzp_test_YDXkxuHKACEffg")
//
//    val objectJson = JSONObject()
//    try {
//        objectJson.put("name", "YummyBites")
//        objectJson.put("description", "Payment for Food")
//        objectJson.put("theme.color", "")
//        objectJson.put("currency", "INR")
//        objectJson.put("amount", (roundedAmount.toDouble() * 100).toInt())
//        objectJson.put("prefill.contact", "1234567890")
//        objectJson.put("prefill.email", email)
//
//        checkout.open(context as Activity?, objectJson)
//
//     } catch (e: JSONException) {
//        e.printStackTrace()
//        Toast.makeText(context, "Error processing payment", Toast.LENGTH_SHORT).show()
//    }
//}

@SuppressLint("SuspiciousIndentation")
fun initiateUpiPayment(
    context: Context,
    amount: String,
    upiId: String,
    name: String,
    description: String,
    paymentStatusListener: PaymentStatusListener
) {
    val paymentAppPackageNames = listOf(
        "com.google.android.apps.nbu.paisa.user",
        "net.one97.paytm",
        "com.phonepe.app",
        "in.org.npci.upiapp",
        "com.bharatpe.customer",
        "com.amazon.mShop.android.shopping",
        "com.mobikwik_new"
     )
        if (!arePaymentAppsInstalled(context, paymentAppPackageNames)) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(context, "App needed to make payment were not found on this device. Please install a UPI payment app to proceed.", Toast.LENGTH_SHORT).show()

        }

        return
    }


        try {
            val transactionId = generateTransactionId()
            val easyUpiPayment = EasyUpiPayment(context as Activity) {
                this.paymentApp = PaymentApp.ALL
                this.payeeVpa = upiId
                this.payeeName = name
                this.transactionId = transactionId
                this.transactionRefId = transactionId
                this.payeeMerchantCode = "BCR2DN4T67UPN5Q3"
                this.description = description
                this.amount = amount
            }

            easyUpiPayment.setPaymentStatusListener(paymentStatusListener)
            easyUpiPayment.startPayment()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error processing payment", Toast.LENGTH_SHORT).show()
        }

}

// Function to generate a fixed-length numeric string
fun generateFixedLengthNumericString(length: Int): String {
    val allowedChars = ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}
fun generateTransactionId(): String {
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("ddMMyyyyHHmmss", Locale.getDefault())
    return dateFormat.format(calendar.time)
}

private fun arePaymentAppsInstalled(context: Context, packageNames: List<String>): Boolean {
    val packageManager = context.packageManager
    for (packageName in packageNames) {
        try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
             return true
        } catch (e: PackageManager.NameNotFoundException) {
         }
    }
     return false
}