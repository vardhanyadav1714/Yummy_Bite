package com.example.yummybites.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

//class ProfileViewModel : ViewModel() {
//    // ViewModel logic for managing profile data and saving to Firebase
//}


@Composable
fun ProfileScreen() {
    var isEditing by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("John Doe") }
    var email by remember { mutableStateOf("johndoe@example.com") }
    // Add more fields as needed (age, etc.)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Profile Image
        ProfileImage(isEditing = isEditing, onImageSelected = {
            // Handle image selection logic here
        })

        // Profile Details
        ProfileDetailItem(label = "Name", value = name, isEditing = isEditing) {
            name = it
        }
        ProfileDetailItem(label = "Email", value = email, isEditing = isEditing) {
            email = it
        }
        // Add more ProfileDetailItems for other fields

        // Edit/Save Button
        IconButton(
            onClick = {
                if (isEditing) {
                    // Save data to Firebase
                    //viewModel.saveProfileData(name, email, /* other fields */)
                }
                isEditing = !isEditing
            }
        ) {
            Icon(
                imageVector = if (isEditing) Icons.Default.Save else Icons.Default.Edit,
                contentDescription = if (isEditing) "Save" else "Edit"
            )
        }
    }
}

@Composable
fun ProfileImage(isEditing: Boolean, onImageSelected: () -> Unit) {
    // Profile image logic, allow image selection in editing mode
}

@Composable
fun ProfileDetailItem(label: String, value: String, isEditing: Boolean, onValueChange: (String) -> Unit) {
    if (isEditing) {
        BasicTextField(
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    // Handle keyboard done action if needed
                }
            ),
            textStyle = TextStyle(fontSize = 18.sp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
    } else {
        Text(
            text = "$label: $value",
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
    }
}
