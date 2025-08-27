package com.yummybitekiet.foodapp.navigation

enum class YummyBitesScreens {
    LoginScreen,
    HomeScreen,
    SplashScreen,
    DetailScreen,
    UpdateScreen,
    PasswordRecover,
    PasswordCodeScreen,
    BottomNavigationScreen,
    CartScreen,
    ProfileScreen,
    PaymentScreen,
    SignUpScreen,
    FavoriteScreen,
    OrderScreen,
    SearchScreen;
    companion object{
        fun fromRoute(route:String):YummyBitesScreens=
            when(route.substringBefore("/")){
                LoginScreen.name->LoginScreen
                HomeScreen.name->HomeScreen
                SplashScreen.name->SplashScreen
                SearchScreen.name->SearchScreen
                DetailScreen.name->DetailScreen
                UpdateScreen.name->UpdateScreen
                PasswordRecover.name->PasswordRecover
                BottomNavigationScreen.name->BottomNavigationScreen
                PasswordCodeScreen.name->PasswordCodeScreen
                CartScreen.name->CartScreen
                PaymentScreen.name->PaymentScreen
                ProfileScreen.name->ProfileScreen
                OrderScreen.name->OrderScreen
                SignUpScreen.name->SignUpScreen
                FavoriteScreen.name->FavoriteScreen
                null->HomeScreen
                else -> throw IllegalArgumentException("route")
            }
    }
}