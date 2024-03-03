package com.example.yummybites.screens.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yummybites.network.FirebaseManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(private val firebaseManager: FirebaseManager) : ViewModel()  {
    val loadingState = MutableStateFlow(LoadingState.IDLE)
    private val auth: FirebaseAuth = Firebase.auth
    private val _loading= MutableLiveData(false)
    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        username: String,
        phone: String,
        home: () -> Unit
    ) {
        if (_loading.value == false) {
            _loading.value = true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        viewModelScope.launch {
                            // Save additional user information to Firestore
                            firebaseManager.saveUserInformation(email, username, phone) { success ->
                                if (success) {
                                    home()
                                } else {
                                    // Handle saving user information failure
                                }
                            }
                        }
                    } else {
                        // Handle registration failure
                    }
                    _loading.value = false
                }
        }
    }


    fun signInUserWithEmailAndPassword(email:String,password:String,home:()->Unit)=
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            home()
                        } else {

                        }
                    }
            } catch (ex: java.lang.Exception) {

            }
        }
}
