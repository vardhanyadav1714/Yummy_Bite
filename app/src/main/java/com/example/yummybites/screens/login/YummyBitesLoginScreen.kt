package com.example.yummybites.screens.login

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.yummybites.R
import com.example.yummybites.navigation.YummyBitesScreens
import com.example.yummybites.screens.home.DishViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Composable
fun YummyBitesLoginScreen(
    image1: Int,
    image2: Int,
    navController: NavController,
    viewModel: LoginScreenViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary),
        contentAlignment = Alignment.Center
    ) {
        // Background Image (image1)
        Image(
            painter = painterResource(image1),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Logo Image (image2)
        Image(
            painter = painterResource(image2),
            contentDescription = null,
            modifier = Modifier
                .size(155.dp)
                .align(Alignment.TopCenter)
                .padding(top = 48.dp)
        )

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Email Input Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", color = Color.White) },
                singleLine = true,
                textStyle = TextStyle(fontSize = 18.sp, color = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(66.dp)
                    .background(color = Color.Transparent),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        // Handle next action
                    }
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Password Input Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", color = Color.White) },
                singleLine = true,
                textStyle = TextStyle(fontSize = 18.sp, color = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(66.dp)
                    .background(color = Color.Transparent),
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        // Handle done action
                    }
                ),
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible) ImageVector.vectorResource(R.drawable.baseline_remove_red_eye_24) else ImageVector.vectorResource(R.drawable.baseline_visibility_off_24),
                            contentDescription = "Password Visibility"
                        )


                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Login Button
            val isButtonEnabled = email.isNotBlank() && password.isNotBlank()
            RoundedButton(
                text = "Login",
                onClick = {
                    viewModel.signInUserWithEmailAndPassword(email, password) {
                        navController.navigate(YummyBitesScreens.BottomNavigationScreen.name)
                    }
                },

            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.align(Alignment.End)) {
                Text(
                    text = "New User? ",
                    color = Color.White,
                    fontSize = 14.sp
                )
                // Create Account Button
                Text(
                    text = " Create Account",
                    modifier = Modifier.clickable {
                        navController.navigate(YummyBitesScreens.SignUpScreen.name)
                    },
                    color = Color.Cyan,
                    fontSize = 14.sp
                )
            }
        }
    }
}
@Composable
fun RoundedButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(23.dp)
            .height(39.dp)
            .clip(RoundedCornerShape(12.dp))
            ,
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White
        )
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
