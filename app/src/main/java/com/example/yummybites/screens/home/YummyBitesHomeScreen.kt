package com.example.yummybites.screens.home

// YummyBitesHomeScreen.kt
// YummyBitesHomeScreen.kt

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
 import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.yummybites.R
import com.example.yummybites.model.Food

//@Composable
//fun YummyBitesHomeScreen(navController: NavController, viewModel: DishViewModel = hiltViewModel()) {
//    var selectedVendor by remember { mutableStateOf("") }
//
//    // Fetch dishes when the selected vendor changes
//    DisposableEffect(selectedVendor) {
//        if (selectedVendor.isNotBlank()) {
//            viewModel.fetchDishesByVendor(selectedVendor)
//        }
//        onDispose { }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        // Vendor Selection Dropdown
//        VendorSelectionDropdown(
//            vendors = listOf("Vendor 1", "Vendor 2", "Vendor 3", "Vendor 4"),
//            onVendorSelected = { selectedVendor = it }
//        )
//
//        // Display dishes
//        DishList(foodList = viewModel.foodList.collectAsState().value)
//
//        // Add other UI components as needed
//    }
//}
//
//@Composable
//fun DishList(foodList: List<Food>) {
//    LazyColumn {
//        items(foodList) { food ->
//            DishCard(food = food)
//        }
//    }
//}
//
//@Composable
//fun DishCard(food: Food) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(bottom = 16.dp)
//            .clickable {
//                // Handle item click as needed
//            },
//        elevation = 4.dp,
//        shape = MaterialTheme.shapes.medium
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(MaterialTheme.colors.background)
//                .padding(16.dp)
//        ) {
//            // Display Image
//            Image(
//                painter = rememberImagePainter(data = food.imageUrl),
//                contentDescription = null,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(200.dp)
//                    .clip(RoundedCornerShape(4.dp)),
//                contentScale = ContentScale.Crop
//            )
//
//            // Display Dish Details
//            Text(
//                text = food.name,
//                style = TextStyle(
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 18.sp
//                ),
//                modifier = Modifier
//                    .padding(top = 8.dp)
//            )
//            Text(
//                text = "Price: $${food.price}",
//                style = TextStyle(fontSize = 16.sp),
//                modifier = Modifier.padding(top = 4.dp)
//            )
//            // Add other details as needed
//
//            // Add to Cart Button
//            Button(
//                onClick = {
//                    // Handle add to cart click
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 8.dp)
//            ) {
//                Text("Add to Cart")
//            }
//        }
//    }
//}
//
//@Composable
//fun VendorSelectionDropdown(
//    vendors: List<String>,
//    onVendorSelected: (String) -> Unit
//) {
//    var expanded by remember { mutableStateOf(false) }
//    var selectedVendor by remember { mutableStateOf("") }
//
//    Column {
//        Text("Vendor: $selectedVendor", modifier = Modifier.padding(8.dp))
//
//        Box(
//            modifier = Modifier
//                .padding(8.dp)
//                .background(Color.LightGray)
//                .clickable { expanded = true }
//        ) {
//            Text(
//                text = if (selectedVendor.isNotEmpty()) selectedVendor else "Select Vendor",
//                modifier = Modifier.padding(8.dp)
//            )
//            DropdownMenu(
//                expanded = expanded,
//                onDismissRequest = { expanded = false }
//            ) {
//                vendors.forEach { vendor ->
//                    DropdownMenuItem(
//                        onClick = {
//                            selectedVendor = vendor
//                            expanded = false
//                            onVendorSelected(vendor)
//                        }
//                    ) {
//                        Text(text = vendor)
//                    }
//                }
//            }
//        }
//    }
//}
//@Composable
//fun YummyBitesHomeScreen(navController: NavController, viewModel: DishViewModel = hiltViewModel()) {
//    // MutableState to track whether the data fetching is in progress
////    var isFetching by remember { mutableStateOf(true) }
////
////    // Fetch all dishes
////    LaunchedEffect(true) {
////        try {
////            viewModel.fetchAllDishes()
////            // Update isFetching when data fetching completes
////            isFetching = false
////        } catch (e: Exception) {
////            // Handle error if needed
////            isFetching = false
////            e.printStackTrace()
////        }
////    }
//
//
//    // Column composable
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//
//    ) {
//        // Display dishes or an error message based on the data fetching status
////        if (isFetching) {
////            // Display a loading indicator or message while data is being fetched
////            // You can replace this with your custom loading composable
////            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
////        } else {
////            // Display all dishes using the DishList composable
////            DishList(foodList = viewModel.foodList.collectAsState().value)
////        }
//        val dummyFoodList = listOf(
//            Food(name = "Tea", price = "20", imageUrl = "https://www.google.com/imgres?imgurl=https%3A%2F%2Fi0.wp.com%2Fpost.healthline.com%2Fwp-content%2Fuploads%2F2020%2F07%2F1296x728-header.jpg%3Fw%3D1155%26h%3D1528&tbnid=D2pYI6eClCZl7M&vet=12ahUKEwjl_OHSufSCAxXB9qACHdEDA5oQMygaegUIARCvAQ..i&imgrefurl=https%3A%2F%2Fwww.healthline.com%2Fnutrition%2Fis-thai-food-healthy&docid=kdRMbfm6anNPcM&w=1155&h=648&q=food%20images&ved=2ahUKEwjl_OHSufSCAxXB9qACHdEDA5oQMygaegUIARCvAQ", availability = true, vendor = "Vendor 1", addedToCart = false),
//            Food(name = "Coffee", price = "30", imageUrl = "https://www.google.com/imgres?imgurl=https%3A%2F%2Fi0.wp.com%2Fpost.healthline.com%2Fwp-content%2Fuploads%2F2020%2F07%2F1296x728-header.jpg%3Fw%3D1155%26h%3D1528&tbnid=D2pYI6eClCZl7M&vet=12ahUKEwjl_OHSufSCAxXB9qACHdEDA5oQMygaegUIARCvAQ..i&imgrefurl=https%3A%2F%2Fwww.healthline.com%2Fnutrition%2Fis-thai-food-healthy&docid=kdRMbfm6anNPcM&w=1155&h=648&q=food%20images&ved=2ahUKEwjl_OHSufSCAxXB9qACHdEDA5oQMygaegUIARCvAQ", availability = false, vendor = "Vendor 2", addedToCart = false),
//            Food(name = "Idli", price = "40", imageUrl = "https://www.google.com/imgres?imgurl=https%3A%2F%2Fi0.wp.com%2Fpost.healthline.com%2Fwp-content%2Fuploads%2F2020%2F07%2F1296x728-header.jpg%3Fw%3D1155%26h%3D1528&tbnid=D2pYI6eClCZl7M&vet=12ahUKEwjl_OHSufSCAxXB9qACHdEDA5oQMygaegUIARCvAQ..i&imgrefurl=https%3A%2F%2Fwww.healthline.com%2Fnutrition%2Fis-thai-food-healthy&docid=kdRMbfm6anNPcM&w=1155&h=648&q=food%20images&ved=2ahUKEwjl_OHSufSCAxXB9qACHdEDA5oQMygaegUIARCvAQ", availability = true, vendor = "Vendor 3", addedToCart = false),
//            Food(name = "Dosa", price = "25", imageUrl = "https://www.google.com/imgres?imgurl=https%3A%2F%2Fi0.wp.com%2Fpost.healthline.com%2Fwp-content%2Fuploads%2F2020%2F07%2F1296x728-header.jpg%3Fw%3D1155%26h%3D1528&tbnid=D2pYI6eClCZl7M&vet=12ahUKEwjl_OHSufSCAxXB9qACHdEDA5oQMygaegUIARCvAQ..i&imgrefurl=https%3A%2F%2Fwww.healthline.com%2Fnutrition%2Fis-thai-food-healthy&docid=kdRMbfm6anNPcM&w=1155&h=648&q=food%20images&ved=2ahUKEwjl_OHSufSCAxXB9qACHdEDA5oQMygaegUIARCvAQ", availability = false, vendor = "Vendor 4", addedToCart = false),
//            Food(name = "Patties", price = "35", imageUrl = "https://www.google.com/imgres?imgurl=https%3A%2F%2Fi0.wp.com%2Fpost.healthline.com%2Fwp-content%2Fuploads%2F2020%2F07%2F1296x728-header.jpg%3Fw%3D1155%26h%3D1528&tbnid=D2pYI6eClCZl7M&vet=12ahUKEwjl_OHSufSCAxXB9qACHdEDA5oQMygaegUIARCvAQ..i&imgrefurl=https%3A%2F%2Fwww.healthline.com%2Fnutrition%2Fis-thai-food-healthy&docid=kdRMbfm6anNPcM&w=1155&h=648&q=food%20images&ved=2ahUKEwjl_OHSufSCAxXB9qACHdEDA5oQMygaegUIARCvAQ", availability = true, vendor = "Vendor 1", addedToCart = false),
//            Food(name = "Bread Pakoda", price = "25", imageUrl = "https://www.google.com/imgres?imgurl=https%3A%2F%2Fi0.wp.com%2Fimage.shutterstock.com%2Fimage-photo%2Findian-tea-time-snack-bread-260nw-442529361.jpg", availability = true, vendor = "Vendor 2", addedToCart = false),
//            Food(name = "Samosa", price = "30", imageUrl = "https://www.google.com/imgres?imgurl=https%3A%2F%2Fi0.wp.com%2Fimage.shutterstock.com%2Fimage-photo%2Findian-tea-time-snack-samosa-260nw-324884473.jpg", availability = false, vendor = "Vendor 3", addedToCart = false),
//            Food(name = "Aloo Tikki", price = "40", imageUrl = "https://www.google.com/imgres?imgurl=https%3A%2F%2Fi0.wp.com%2Fimage.shutterstock.com%2Fimage-photo%2Fpopular-indian-snack-aloo-tikki-260nw-181833468.jpg", availability = true, vendor = "Vendor 4", addedToCart = false),
//            Food(name = "Kachori", price = "35", imageUrl = "https://www.google.com/imgres?imgurl=https%3A%2F%2Fwww.vidhyashomecooking.com%2Fwp-content%2Fuploads%2F2015%2F09%2Furad-dal-kachori.jpg", availability = false, vendor = "Vendor 1", addedToCart = false),
//            // Add more foods as needed
//        )
//        DishList(foodList = dummyFoodList)
//        // Add other UI components as needed
//    }
//}

//@Composable
//fun DishList(foodList: List<Food>) {
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        Text(
//            text = "Featured Dishes",
//            style = TextStyle(
//                fontSize = 20.sp,
//                fontWeight = FontWeight.Bold
//            ),
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//
//        LazyColumn(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            items(items = foodList.windowed(2, 2, partialWindows = true)) { rowItems ->
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(bottom = 8.dp),
//                    horizontalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    for (food in rowItems) {
//                        DishCard(food = food )
//                    }
//                }
//            }
//        }
//    }
//}
//
//
//@Composable
//fun DishCard(
//    food: Food,
//
//) {
//    Card(
//        shape = RoundedCornerShape(14.dp),
//        backgroundColor = Color.White,
//        modifier = Modifier
//            .width(180.dp)
//
//        , elevation = 2.dp
//    ) {
//        Column(
//            modifier = Modifier
//                .padding(16.dp)
//                .fillMaxSize()
//        ) {
//            Image(
//                painter = painterResource(id = R.drawable.download),
//                contentDescription = null,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(120.dp)
//                    .clip(CircleShape), // Circular shape
//                contentScale = ContentScale.Crop
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(
//                text = food.name,
//                style = TextStyle(
//                    color = Color.Gray,
//                    fontSize = 16.sp
//                )
//            )
//            Spacer(modifier = Modifier.height(4.dp))
//            Text(
//                text = food.price,
//                style = TextStyle(
//                    color = Color.Blue,
//                    fontSize = 16.sp
//                )
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//            AvailabilityTextSurface(
//                availability = food.availability,
//                modifier = Modifier.align(Alignment.End)
//            )
//        }
//    }
//}
//
//@Composable
//fun AvailabilityTextSurface(
//    availability: Boolean,
//    modifier: Modifier = Modifier
//) {
//    val backgroundColor = if (availability) Color.Green else Color.Red
//    val content = if (availability) "Available" else "Not Available"
//
//    Surface(
//        modifier = modifier.padding(4.dp),
//        shape = RoundedCornerShape(12.dp),
//        color = backgroundColor,
//        contentColor = Color.White
//    ) {
//        Text(
//            text = content,
//            modifier = Modifier.padding(8.dp),
//            fontSize = 12.sp
//        )
//    }
//}
//@Composable
//fun YummyBitesHomeScreen(navController: NavController,viewModel: DishViewModel= hiltViewModel()) {
//    var selectedVendor by remember { mutableStateOf("") }
//    val defaultVendor = "Vendor 1"
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        // Vendor Selection Dropdown
//        VendorSelectionDropdown(
//            vendors = listOf("Vendor 1", "Vendor 2", "Vendor 3", "Vendor 4"),
//            onVendorSelected = { selectedVendor = it }
//        )
//
//        val dummyFoodList = listOf(
//            Food(name = "Tea", price = "20", imageUrl = "https://www.google.com/imgres?imgurl=https%3A%2F%2Fi0.wp.com%2Fpost.healthline.com%2Fwp-content%2Fuploads%2F2020%2F07%2F1296x728-header.jpg%3Fw%3D1155%26h%3D1528&tbnid=D2pYI6eClCZl7M&vet=12ahUKEwjl_OHSufSCAxXB9qACHdEDA5oQMygaegUIARCvAQ..i&imgrefurl=https%3A%2F%2Fwww.healthline.com%2Fnutrition%2Fis-thai-food-healthy&docid=kdRMbfm6anNPcM&w=1155&h=648&q=food%20images&ved=2ahUKEwjl_OHSufSCAxXB9qACHdEDA5oQMygaegUIARCvAQ", availability = true, vendor = "Vendor 1", addedToCart = false),
//            Food(name = "Coffee", price = "30", imageUrl = "https://www.google.com/imgres?imgurl=https%3A%2F%2Fi0.wp.com%2Fpost.healthline.com%2Fwp-content%2Fuploads%2F2020%2F07%2F1296x728-header.jpg%3Fw%3D1155%26h%3D1528&tbnid=D2pYI6eClCZl7M&vet=12ahUKEwjl_OHSufSCAxXB9qACHdEDA5oQMygaegUIARCvAQ..i&imgrefurl=https%3A%2F%2Fwww.healthline.com%2Fnutrition%2Fis-thai-food-healthy&docid=kdRMbfm6anNPcM&w=1155&h=648&q=food%20images&ved=2ahUKEwjl_OHSufSCAxXB9qACHdEDA5oQMygaegUIARCvAQ", availability = false, vendor = "Vendor 2", addedToCart = false),
//            Food(name = "Idli", price = "40", imageUrl = "https://www.google.com/imgres?imgurl=https%3A%2F%2Fi0.wp.com%2Fpost.healthline.com%2Fwp-content%2Fuploads%2F2020%2F07%2F1296x728-header.jpg%3Fw%3D1155%26h%3D1528&tbnid=D2pYI6eClCZl7M&vet=12ahUKEwjl_OHSufSCAxXB9qACHdEDA5oQMygaegUIARCvAQ..i&imgrefurl=https%3A%2F%2Fwww.healthline.com%2Fnutrition%2Fis-thai-food-healthy&docid=kdRMbfm6anNPcM&w=1155&h=648&q=food%20images&ved=2ahUKEwjl_OHSufSCAxXB9qACHdEDA5oQMygaegUIARCvAQ", availability = true, vendor = "Vendor 3", addedToCart = false),
//            Food(name = "Dosa", price = "25", imageUrl = "https://www.google.com/imgres?imgurl=https%3A%2F%2Fi0.wp.com%2Fpost.healthline.com%2Fwp-content%2Fuploads%2F2020%2F07%2F1296x728-header.jpg%3Fw%3D1155%26h%3D1528&tbnid=D2pYI6eClCZl7M&vet=12ahUKEwjl_OHSufSCAxXB9qACHdEDA5oQMygaegUIARCvAQ..i&imgrefurl=https%3A%2F%2Fwww.healthline.com%2Fnutrition%2Fis-thai-food-healthy&docid=kdRMbfm6anNPcM&w=1155&h=648&q=food%20images&ved=2ahUKEwjl_OHSufSCAxXB9qACHdEDA5oQMygaegUIARCvAQ", availability = false, vendor = "Vendor 4", addedToCart = false),
//            Food(name = "Patties", price = "35", imageUrl = "https://www.google.com/imgres?imgurl=https%3A%2F%2Fi0.wp.com%2Fpost.healthline.com%2Fwp-content%2Fuploads%2F2020%2F07%2F1296x728-header.jpg%3Fw%3D1155%26h%3D1528&tbnid=D2pYI6eClCZl7M&vet=12ahUKEwjl_OHSufSCAxXB9qACHdEDA5oQMygaegUIARCvAQ..i&imgrefurl=https%3A%2F%2Fwww.healthline.com%2Fnutrition%2Fis-thai-food-healthy&docid=kdRMbfm6anNPcM&w=1155&h=648&q=food%20images&ved=2ahUKEwjl_OHSufSCAxXB9qACHdEDA5oQMygaegUIARCvAQ", availability = true, vendor = "Vendor 1", addedToCart = false),
//            Food(name = "Bread Pakoda", price = "25", imageUrl = "https://www.google.com/imgres?imgurl=https%3A%2F%2Fi0.wp.com%2Fimage.shutterstock.com%2Fimage-photo%2Findian-tea-time-snack-bread-260nw-442529361.jpg", availability = true, vendor = "Vendor 2", addedToCart = false),
//            Food(name = "Samosa", price = "30", imageUrl = "https://www.google.com/imgres?imgurl=https%3A%2F%2Fi0.wp.com%2Fimage.shutterstock.com%2Fimage-photo%2Findian-tea-time-snack-samosa-260nw-324884473.jpg", availability = false, vendor = "Vendor 3", addedToCart = false),
//            Food(name = "Aloo Tikki", price = "40", imageUrl = "https://www.google.com/imgres?imgurl=https%3A%2F%2Fi0.wp.com%2Fimage.shutterstock.com%2Fimage-photo%2Fpopular-indian-snack-aloo-tikki-260nw-181833468.jpg", availability = true, vendor = "Vendor 4", addedToCart = false),
//            Food(name = "Kachori", price = "35", imageUrl = "https://www.google.com/imgres?imgurl=https%3A%2F%2Fwww.vidhyashomecooking.com%2Fwp-content%2Fuploads%2F2015%2F09%2Furad-dal-kachori.jpg", availability = false, vendor = "Vendor 1", addedToCart = false),
//            Food(name = "Chowmein", price = "50", imageUrl = "https://www.example.com/chowmein.jpg", availability = true, vendor = "Vendor 2", addedToCart = false),
//            Food(name = "Biryani", price = "80", imageUrl = "https://www.example.com/biryani.jpg", availability = true, vendor = "Vendor 3", addedToCart = false),
//            Food(name = "Cake", price = "40", imageUrl = "https://www.example.com/cake.jpg", availability = true, vendor = "Vendor 4", addedToCart = false),
//            Food(name = "Pastry", price = "25", imageUrl = "https://www.example.com/pastry.jpg", availability = true, vendor = "Vendor 1", addedToCart = false),
//            Food(name = "Gulab Jamun", price = "30", imageUrl = "https://www.example.com/gulabjamun.jpg", availability = true, vendor = "Vendor 2", addedToCart = false),
//            Food(name = "Veg Roll", price = "35", imageUrl = "https://www.example.com/vegroll.jpg", availability = true, vendor = "Vendor 3", addedToCart = false),
//            Food(name = "Paneer Tikka", price = "60", imageUrl = "https://www.example.com/paneertikka.jpg", availability = true, vendor = "Vendor 4", addedToCart = false),
//            Food(name = "Ice Cream", price = "20", imageUrl = "https://www.example.com/icecream.jpg", availability = true, vendor = "Vendor 1", addedToCart = false),
//            Food(name = "Cream Roll", price = "15", imageUrl = "https://www.example.com/creamroll.jpg", availability = true, vendor = "Vendor 2", addedToCart = false),
//            Food(name = "Paratha", price = "30", imageUrl = "https://www.example.com/paratha.jpg", availability = true, vendor = "Vendor 3", addedToCart = false),
//            Food(name = "Lassi", price = "10", imageUrl = "https://www.example.com/lassi.jpg", availability = true, vendor = "Vendor 4", addedToCart = false),
//            Food(name = "Hot Dog", price = "40", imageUrl = "https://www.example.com/hotdog.jpg", availability = true, vendor = "Vendor 1", addedToCart = false),
//            Food(name = "Donuts", price = "20", imageUrl = "https://www.example.com/donuts.jpg", availability = true, vendor = "Vendor 2", addedToCart = false),
//            Food(name = "Baguette", price = "30", imageUrl = "https://www.example.com/baguette.jpg", availability = true, vendor = "Vendor 3", addedToCart = false),
//            Food(name = "Soft Drink", price = "15", imageUrl = "https://www.example.com/softdrink.jpg", availability = true, vendor = "Vendor 4", addedToCart = false),
//            Food(name = "Onion Ring", price = "25", imageUrl = "https://www.example.com/onionring.jpg", availability = true, vendor = "Vendor 1", addedToCart = false),
//            Food(name = "Sausage", price = "35", imageUrl = "https://www.example.com/sausage.jpg", availability = true, vendor = "Vendor 2", addedToCart = false),
//            Food(name = "Pretzel", price = "30", imageUrl = "https://www.example.com/pretzel.jpg", availability = true, vendor = "Vendor 3", addedToCart = false),
//            Food(name = "Pancake", price = "25", imageUrl = "https://www.example.com/pancake.jpg", availability = true, vendor = "Vendor 4", addedToCart = false),
//            Food(name = "Bacon", price = "45", imageUrl = "https://www.example.com/bacon.jpg", availability = true, vendor = "Vendor 1", addedToCart = false),
//            Food(name = "Noodle", price = "55", imageUrl = "https://www.example.com/noodle.jpg", availability = true, vendor = "Vendor 2", addedToCart = false)
//        )
//
//
//        // Display dishes based on selected vendor
//        val filteredFoodList = dummyFoodList.filter { it.vendor == selectedVendor }
//        DishList(foodList = filteredFoodList, onAddToCartClick = { viewModel.onAddToCartClick(it)  /* Handle add to cart click */ })
//
//        // Add other UI components as needed
//    }
//}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun YummyBitesHomeScreen(navController: NavController, viewModel: DishViewModel = hiltViewModel()) {
    var searchText by remember { mutableStateOf("") }
     var showFilterDialog by remember { mutableStateOf(false) }
    val keyboardController=LocalSoftwareKeyboardController.current
     var allDishes by remember { mutableStateOf<List<Food>>(emptyList()) }
    var selectedVendor by remember { mutableStateOf("Vendor 1") }
    var minPrice by remember { mutableStateOf("0") }
    var maxPrice by remember { mutableStateOf("1000") }
    LaunchedEffect(true) {
        viewModel.fetchAllDishes()
    }

    LaunchedEffect(true) {
        viewModel.allDishes.collect { dishes ->
            allDishes = dishes
        }
     }



    var filteredDishes by remember { mutableStateOf(emptyList<Food>()) } // Initialize with empty list


    // Update filtered dishes whenever all dishes change or search text changes
    LaunchedEffect(allDishes, searchText) {
        filteredDishes = if (searchText.isNotBlank()) {
            allDishes.filter { it.name.contains(searchText, ignoreCase = true) }
        } else {
            allDishes
        }
    }
    LaunchedEffect(selectedVendor, minPrice, maxPrice) {
        filteredDishes = allDishes.filter { food ->
            val minPriceFloat = if (minPrice.isNotEmpty()) minPrice.toFloat() else Float.MIN_VALUE
            val maxPriceFloat = if (maxPrice.isNotEmpty()) maxPrice.toFloat() else Float.MAX_VALUE
            (selectedVendor.isEmpty() || food.vendor == selectedVendor) &&
                    (minPrice.isEmpty() || food.price.toFloat() >= minPriceFloat) &&
                    (maxPrice.isEmpty() || food.price.toFloat() <= maxPriceFloat)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top App Bar with Search and Filter Icon
       Row() {
            IconButton(onClick = { showFilterDialog = true }) {
                Icon(Icons.Filled.FilterList, contentDescription = "Filter")
            }
           OutlinedTextField(value = searchText, onValueChange = {searchText=it}, label = {Text(text = "Search Dish", fontStyle = FontStyle.Normal)},
               singleLine = true, modifier = Modifier
                   .height(64.dp)
                   .fillMaxWidth()
               , keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text, imeAction = ImeAction.Search), keyboardActions = KeyboardActions(onNext ={keyboardController?.hide()})
           )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display filter dialog if showFilterDialog is true
        if (showFilterDialog) {
            FilterDialog(
                selectedVendor = selectedVendor,
                onDismiss = { showFilterDialog = false },
                onApplyClicked = { vendor, min, max ->
                    // Apply filters here
                    selectedVendor = vendor
                    minPrice = min.toString()
                    maxPrice = max.toString()
                },
                onVendorSelected = { selectedVendor = it }
            )
        }

        if (allDishes.isEmpty()) {
            // Display a loading indicator or message while data is being fetched
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            // Display filtered or searched dishes based on search text and filters
            DishList(
                foodList = filteredDishes,
                onAddToCartClick = { viewModel.onAddToCartClick(it) }
            )

        }
    }
}

@Composable
fun DishList(foodList: List<Food>, onAddToCartClick: (Food) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(items = foodList.windowed(2, 2, partialWindows = true)) { rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (food in rowItems) {

                    DishCard(food = food)
                }
            }
        }
    }
}

@Composable
fun FilterDialog(
    selectedVendor: String,
    onDismiss: () -> Unit,
    onApplyClicked: (String, Float, Float) -> Unit,
    onVendorSelected: (String) -> Unit
) {
    var minPrice by remember { mutableStateOf("") }
    var maxPrice by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filter") },
        text = {
            Column {
                Text("Vendor:")
                Spacer(modifier = Modifier.height(8.dp))
                VendorDropdown(
                    selectedVendor = selectedVendor,
                    onVendorSelected = onVendorSelected
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Price Range:")
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = minPrice,
                        onValueChange = { minPrice = it },
                        label = { Text("Min Price") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = maxPrice,
                        onValueChange = { maxPrice = it },
                        label = { Text("Max Price") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                println("Applying filter with selectedVendor: $selectedVendor, minPrice: $minPrice, maxPrice: $maxPrice")
                onApplyClicked(
                    selectedVendor,
                    minPrice.toFloatOrNull() ?: 0f,
                    maxPrice.toFloatOrNull() ?: Float.MAX_VALUE
                )
                onDismiss() // Move this line here
            }) {
                Text("Apply")
            }

        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        modifier = Modifier.padding(16.dp) // Add padding
    )
}

@Composable
fun VendorDropdown(
    selectedVendor: String,
    onVendorSelected: (String) -> Unit
) {
    val vendors = listOf("Vendor 1", "Vendor 2", "Vendor 3", "Vendor 4")
    var expanded by remember { mutableStateOf(false) }

    Box {
        TextButton(onClick = { expanded = true }) {
            Text(text = if (selectedVendor.isEmpty()) "Select Vendor" else selectedVendor)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            vendors.forEach { vendor ->
                DropdownMenuItem(
                    onClick = {
                        onVendorSelected(vendor)
                        expanded = false
                    }
                ) {
                    Text(text = vendor)
                }
            }
        }
    }
}



@Composable
fun DishCard(
    food: Food,
    viewModel: DishViewModel = hiltViewModel()
) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .padding(bottom = 16.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.down),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = food.name,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.primary
                ),
                modifier = Modifier.padding(start = 8.dp, end = 8.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Price: ₹${food.price}",
                style = TextStyle(
                    fontSize = 14.sp,
                    color = MaterialTheme.colors.secondary
                ),
                modifier = Modifier.padding(start = 8.dp, end = 8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, end = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Add to Cart Button
                Button(
                    onClick = {
                        if (food.availability && !food.addedToCart) {
                            viewModel.onAddToCartClick(food)
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (food.addedToCart) Color.Gray else MaterialTheme.colors.primary,
                        contentColor = MaterialTheme.colors.onPrimary
                    ),
                    enabled = food.availability && !food.addedToCart
                ) {
                    Text(if (food.addedToCart) "Added" else "+Cart")
                }

                // Availability Text
                AvailabilityTextSurface(
                    availability = food.availability,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
    }
}

@Composable
fun AvailabilityTextSurface(
    availability: Boolean,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (availability) Color.Green else Color.Red
    val content = if (availability) "Available" else "Not Available"

    Box(
        modifier = modifier
            .background(color = Color.Transparent, shape = RoundedCornerShape(12.dp))
            .border(width = 1.dp, color = backgroundColor, shape = RoundedCornerShape(12.dp))
    ) {
        Text(
            text = content,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .wrapContentSize(Alignment.Center),
            color = backgroundColor,
            fontSize = 12.sp
        )
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterBottomSheetDialog(
    selectedVendor: String,
    onDismiss: () -> Unit,
    onApplyClicked: (String, Float, Float) -> Unit,
    onVendorSelected: (String) -> Unit
) {
    var minPrice by remember { mutableStateOf("") }
    var maxPrice by remember { mutableStateOf("") }

    ModalBottomSheetLayout(
        sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden),
        modifier = Modifier.fillMaxHeight(0.9f),
                sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Headings Column
                Column {
                    Text("Filter", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Vendor", fontWeight = FontWeight.Bold)
                }

                // Vendor Selection LazyColumn
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                ) {
                    itemsIndexed(items = listOf("Vendor 1", "Vendor 2", "Vendor 3", "Vendor 4")) { index, vendor ->
                        val isSelected = vendor == selectedVendor
                        VendorItem(
                            vendor = vendor,
                            isSelected = isSelected,
                            onVendorSelected = onVendorSelected
                        )
                        if (index < 3) {
                            Divider(color = Color.LightGray, thickness = 1.dp)
                        }
                    }
                }

                // Price Range Column
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Price Range:", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = minPrice,
                            onValueChange = { minPrice = it },
                            label = { Text("Min Price") },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = maxPrice,
                            onValueChange = { maxPrice = it },
                            label = { Text("Max Price") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        onApplyClicked(
                            selectedVendor,
                            minPrice.toFloatOrNull() ?: 0f,
                            maxPrice.toFloatOrNull() ?: Float.MAX_VALUE
                        )
                        onDismiss()
                    }) {
                        Text("Apply")
                    }
                }
            }
        },
        content = {
            /* Your main content goes here */
        },
        // Add your other parameters as needed
    )
}

@Composable
fun VendorItem(
    vendor: String,
    isSelected: Boolean,
    onVendorSelected: (String) -> Unit
) {
    val backgroundColor = if (isSelected) Color.LightGray else Color.Transparent
    val textColor = if (isSelected) Color.Black else Color.Gray

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = backgroundColor)
            .clickable { onVendorSelected(vendor) }
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Text(
            text = vendor,
            color = textColor,
            fontSize = 16.sp
        )
    }
}

