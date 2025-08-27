package com.yummybitekiet.foodapp.screens

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yummybitekiet.foodapp.navigation.YummyBitesScreens
import com.yummybitekiet.foodapp.screens.login.LoginScreenViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignUpScreen(
    image1: Int,
    image2: Int,
    navController: NavController,
    viewModel: LoginScreenViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isRepeatPasswordVisible by remember { mutableStateOf(false) }
    val keyboardController=LocalSoftwareKeyboardController.current
    val context= LocalContext.current
    val isButtonEnabled by remember(email, password, username, phone, repeatPassword) {
        mutableStateOf(
            email.isNotBlank() && email.contains("@kiet.edu") &&
                    isEmailValid(email) &&
                    password.isNotBlank() &&
                    username.isNotBlank() &&
                    phone.isNotBlank() &&
                    repeatPassword.isNotBlank() &&
                    email.length >= 20 &&
                    phone.length == 10 &&
                    username.length > 8 &&
                    password.length > 10 &&
                    repeatPassword == password &&
                    phone.firstOrNull()?.isDigit() == true && // Check if the first character is a digit
                    validCellPhone(phone)
        )
    }
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
                    onNext = {                         keyboardController?.hide()
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
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {keyboardController?.hide() }
                ),
                trailingIcon = {
                    PasswordVisibilityIcon(isPasswordVisible) {
                        isPasswordVisible = !isPasswordVisible
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Repeat Password Input Field
            OutlinedTextField(
                value = repeatPassword,
                onValueChange = { repeatPassword = it },
                label = { Text("Repeat Password", color = Color.White) },
                singleLine = true,
                textStyle = TextStyle(fontSize = 18.sp, color = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(66.dp)
                    .background(color = Color.Transparent),
                visualTransformation = if (isRepeatPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { keyboardController?.hide() }
                ),
                trailingIcon = {
                    PasswordVisibilityIcon(isRepeatPasswordVisible) {
                        isRepeatPasswordVisible = !isRepeatPasswordVisible
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Username Input Field
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username", color = Color.White) },
                singleLine = true,
                textStyle = TextStyle(fontSize = 18.sp, color = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(66.dp)
                    .background(color = Color.Transparent),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {keyboardController?.hide()
                    }
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Phone Input Field
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone", color = Color.White) },
                singleLine = true,
                textStyle = TextStyle(fontSize = 18.sp, color = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(66.dp)
                    .background(color = Color.Transparent),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Create Account Button

            RoundedButton(
                text = "Create Account",
                onClick = {
                    if(isButtonEnabled){
                     // Implement create account logic here
                     viewModel.createUserWithEmailAndPassword(
                        email = email,
                        password = password,
                        username = username,
                        phone = phone
                    ) {
                         navController.navigate(YummyBitesScreens.BottomNavigationScreen.name)
                    }
                     }else{
                         if(email.isBlank() && password.isBlank() &&  repeatPassword.isBlank() && phone.isBlank() && username.isBlank()){
                             Toast.makeText(context,"All the fields are empty please enter all fields in order to continue",Toast.LENGTH_SHORT).show()
                         }
                        if(email.isBlank() && password.isBlank()){
                            Toast.makeText(context,"Email and Passowrd Fields are empty please enter in order to continue",Toast.LENGTH_SHORT).show()
                        }
                        if(email.isBlank() && repeatPassword.isBlank()){
                            Toast.makeText(context,"Email and Repeat Password Fields are empty please enter in order to continue",Toast.LENGTH_SHORT).show()
                        }
                        if(email.isBlank() && phone.isBlank()){
                            Toast.makeText(context,"Email and Phone Fields are empty pelase enter in order to continue",Toast.LENGTH_SHORT).show()
                        }
                        if(email.isBlank() && username.isBlank()){
                            Toast.makeText(context,"Email and Password Fields are empty please enter in order to continue",Toast.LENGTH_SHORT).show()
                        }
                        if(password.isBlank() && repeatPassword.isBlank()){
                            Toast.makeText(context,"Passowrd and Repeat Password Fields ar empty pelase enter in order to continue",Toast.LENGTH_SHORT).show()
                        }
                        if(repeatPassword.isBlank() && phone.isBlank()){
                            Toast.makeText(context,"Repeat Password and Phone Fields are empty please enter in order to continue",Toast.LENGTH_SHORT).show()
                        }
                         if(phone.first()<5.toChar() ){
                             Toast.makeText(context,"Please enter correct mobile number in order to proceed",Toast.LENGTH_SHORT).show()
                         }
                         if(email.length <20 && phone.length !=10 && username.length<=8){
                             Toast.makeText(context,"Please enter the correct email, phone, and username in order to continue",Toast.LENGTH_SHORT).show()
                         }
                        if(!email.contains("@kiet.edu")){
                            Toast.makeText(context,"Please enter institute email to continue",Toast.LENGTH_SHORT).show()
                        }
                        if(email.isBlank()){
                            Toast.makeText(context,"Please enter email to continue",Toast.LENGTH_SHORT).show()
                        }
                        if(password.isBlank()){
                            Toast.makeText(context,"Please enter password in order to continue",Toast.LENGTH_SHORT).show()
                        }
                        if(repeatPassword.isBlank()){
                            Toast.makeText(context,"You cannot leave repeat password field blank",Toast.LENGTH_SHORT).show()
                        }
                        if(phone.isBlank()){
                            Toast.makeText(context,"Please enter your mobile number in order to continue",Toast.LENGTH_SHORT).show()
                        }
                        if(username.isBlank()){
                            Toast.makeText(context,"Please enter proper username to continue",Toast.LENGTH_SHORT).show()
                        }
                        if(username.length <=8 ){
                            Toast.makeText(context,"This username is not available please enter another username with longer length",Toast.LENGTH_SHORT).show()

                        }
                        if(phone.length !=10){
                            Toast.makeText(context,"The mobile number that you entered is incorrect pelase enter a correct mobile number",Toast.LENGTH_SHORT).show()
                        }
                        if(email.length<=20){
                            Toast.makeText(context,"The email that you entered is incorrect pelase enter correct emial in order to continue",Toast.LENGTH_SHORT).show()

                        }
                        if(password.length<10){
                            Toast.makeText(context,"The password that you entered is either too short or too weak",Toast.LENGTH_SHORT).show()

                        }
                        if(!repeatPassword.equals(password)){
                            Toast.makeText(context,"Your reapeat passowrd does not matches your passowrd.",Toast.LENGTH_SHORT).show()

                        }
                        if(!isEmailValid(email)){
                            Toast.makeText(context,"The email that you entered is not valid, Please enter a valid email addresss",Toast.LENGTH_SHORT).show()
                        }
                        if(!validCellPhone(phone)){
                            Toast.makeText(context,"The phone number that you entered is not valid please enter a valid phone number",Toast.LENGTH_SHORT).show()

                        }
                    }
                },

            )

            Row(modifier = Modifier.align(Alignment.End)) {
                Text(
                    text = "Already Account?  ",
                    color = Color.White,
                    fontSize = 14.sp
                )
                // Create Account Button

                Text(
                    text = "Login",
                    modifier = Modifier.clickable {
                        navController.navigate(YummyBitesScreens.LoginScreen.name)
                    },
                    color = Color.Cyan,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun PasswordVisibilityIcon(isVisible: Boolean, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = if (isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
            contentDescription = "Password Visibility"
        )
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
//fun isEmailValid(email: CharSequence?): Boolean {
//    return Patterns.EMAIL_ADDRESS.matcher(email!!)
//        .matches()
//}
fun validCellPhone(number: String?): Boolean {
    return Patterns.PHONE.matcher(number!!).matches()
}
fun isEmailValid(email: String): Boolean {
    val regex = Regex("""^[a-zA-Z]+\.\d{4}[a-zA-Z]+(\d{4})?@[a-zA-Z0-9.-]+\.(edu)$""")
    return regex.matches(email)
}
