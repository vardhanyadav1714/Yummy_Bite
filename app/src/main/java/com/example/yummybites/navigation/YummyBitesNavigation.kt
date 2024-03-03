package com.example.yummybites.navigation

 

import YourMainScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.yummybites.PasswordCodeScreen
import com.example.yummybites.R
import com.example.yummybites.screens.SignUpScreen
import com.example.yummybites.screens.YummyBitesSplashScreen
import com.example.yummybites.screens.login.YummyBitesLoginScreen
import com.example.yummybites.screens.payments.PaymentsScreen

import com.example.yummybites.screens.update.RecoverPassword


@Composable
fun YummyBitesNavigation() {
    val navController= rememberNavController()
    val navHost= NavHost(navController = navController, startDestination = YummyBitesScreens.SplashScreen.name ){
       val image1= R.drawable.aa
        val image2= R.drawable.bb
        composable(YummyBitesScreens.SplashScreen.name){
            YummyBitesSplashScreen(navController=navController,image1,image2)
        }
        val imag1= R.drawable.aa
        val imag2= R.drawable.bb
        composable(YummyBitesScreens.LoginScreen.name){
            YummyBitesLoginScreen(navController=navController, image1 = imag1, image2 = imag2)
        }

        composable(YummyBitesScreens.PasswordRecover.name){
            RecoverPassword(navController=navController,imag1,imag2)
        }
        val imagee1=R.drawable.aa
            val imagee2=R.drawable.bb
        composable(YummyBitesScreens.PasswordCodeScreen.name){
            PasswordCodeScreen(navController=navController,imagee1,imagee2)
        }
        composable(YummyBitesScreens.BottomNavigationScreen.name) {
           // val yummybitesAction=YummyBitesActions(navController=navController)
            YourMainScreen(navctl=navController)
        }

       composable(YummyBitesScreens.SignUpScreen.name){
           val imagg1= R.drawable.aa
           val imagg2= R.drawable.bb
           SignUpScreen( image1 = imagg1, image2 = imagg2,navController = navController)
       }
    }
}