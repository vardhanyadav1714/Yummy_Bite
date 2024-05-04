package com.example.yummybites.screens.payments
import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.yummybites.*
import com.example.yummybites.model.Food
import com.example.yummybites.model.User
import com.example.yummybites.navigation.YummyBitesScreens
import com.example.yummybites.screens.home.DishViewModel
import com.example.yummybites.screens.profile.ProfileScreenViewModel
import com.razorpay.Checkout
import dev.shreyaspatil.easyupipayment.EasyUpiPayment
import dev.shreyaspatil.easyupipayment.model.PaymentApp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.util.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PaymentsScreen(
    navController: NavHostController,
    viewModel: DishViewModel,
    totallAmount: Double,
    profileviewmodel:ProfileScreenViewModel= hiltViewModel()
     //cartItems: List<Food>
) {
    var email by remember { mutableStateOf("") }
    var orderType by remember {
        mutableStateOf(OrderTpe.PayAtOutlet)
    }
   var createnow by remember {mutableStateOf(false)}
    val context = LocalContext.current
     LaunchedEffect(viewModel) {
        viewModel.getCartItems()
    }
    val cartItems by viewModel.cartitemlist.collectAsState()
    val totalAmount = cartItems.sumBy { it.totalPrice.toInt() }
    var user by remember { mutableStateOf(User("", "", "")) }

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

    val scope = rememberCoroutineScope()
    fun createOrder() {
        if (!createnow) {
            createnow = true
            scope.launch {
                 viewModel.saveOrderForAll(order)
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
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    // Hide keyboard on Done
                    // keyboardController?.hide()
                }
            ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.primary,
                unfocusedBorderColor = MaterialTheme.colors.onSurface
            )
        )

        // Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                     orderType=OrderTpe.PayAtOutlet
                    createOrder()
                    scope.launch {
                        delay(3000)
                        navController.navigate(YummyBitesScreens.OrderScreen.name)

                        navController.navigate(YummyBitesScreens.CartScreen.name)
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
                    if (email.isNotBlank() && email.contains("@")) {
                        // Proceed with paymentX

                         initiatePayment(context,email,totallAmount.toString())
                        orderType=OrderTpe.PaidUsingRazorPay
                         createOrder()
                        scope.launch {
                            delay(1000)
                            navController.navigate(YummyBitesScreens.OrderScreen.name)

                            navController.navigate(YummyBitesScreens.CartScreen.name)
                        }
                    } else {
                        // Show error message for invalid email
                        Toast.makeText(context, "Invalid email address", Toast.LENGTH_SHORT).show()
                    }
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
    }
}



fun initiatePayment(
    context: Context,
    email: String,
    amount: String,
  ) {
    val roundedAmount = "%.2f".format(amount.toDouble())
    val checkout = Checkout()
    checkout.setKeyID("rzp_test_YDXkxuHKACEffg")

    val objectJson = JSONObject()
    try {
        objectJson.put("name", "YummyBites")
        objectJson.put("description", "Payment for Food")
        objectJson.put("theme.color", "")
        objectJson.put("currency", "INR")
        objectJson.put("amount", (roundedAmount.toDouble() * 100).toInt())
        objectJson.put("prefill.contact", "1234567890")
        objectJson.put("prefill.email", email)

        checkout.open(context as Activity?, objectJson)

     } catch (e: JSONException) {
        e.printStackTrace()
        Toast.makeText(context, "Error processing payment", Toast.LENGTH_SHORT).show()
    }
}
fun initiateUpiPayment(
    context: Context,
    amount: String,
    upiId: String,
    name: String,
    description: String,
    mainActivity: MainActivity
) {
    try {
        // START PAYMENT INITIALIZATION
        val easyUpiPayment = EasyUpiPayment(context as Activity) {
            this.paymentApp = PaymentApp.ALL
            this.payeeVpa = upiId
            this.payeeName = name
            this.transactionId = UUID.randomUUID().toString() // Generating a random transaction ID
            this.transactionRefId = UUID.randomUUID().toString() // Generating a random transaction reference ID
            this.payeeMerchantCode = UUID.randomUUID().toString() // Generating a random merchant code
            this.description = description
            this.amount = amount
        }
        // END INITIALIZATION

        // Register Listener for Events
        easyUpiPayment.setPaymentStatusListener(mainActivity)

        // Start payment / transaction
        easyUpiPayment.startPayment()
    } catch (e: Exception) {
        // Handle exceptions
        e.printStackTrace()
        Toast.makeText(context, "Error processing payment", Toast.LENGTH_SHORT).show()
    }
}
