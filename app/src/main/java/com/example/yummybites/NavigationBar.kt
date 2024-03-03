import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.yummybites.screens.home.YummyBitesHomeScreen
import com.example.yummybites.navigation.YummyBitesScreens

import com.example.yummybites.screens.OrderScreen
import com.example.yummybites.screens.cart.CartScreen
import com.example.yummybites.screens.payments.PaymentsScreen
import com.example.yummybites.screens.profile.ProfileScreen

class YummyBitesActions(navController: NavController) {
    val openLogin: () -> Unit = {
        navController.navigate(YummyBitesScreens.LoginScreen.name)
    }
    val openHome: () -> Unit = {
        navController.navigate(YummyBitesScreens.HomeScreen.name)
    }
    val openProfile: () -> Unit = {
        navController.navigate(YummyBitesScreens.ProfileScreen.name)
    }
    val openCart: () -> Unit = {
        navController.navigate(YummyBitesScreens.CartScreen.name)
    }
}

@Composable
fun YourMainScreen(navctl:NavController) {
    val navController = rememberNavController()

    val bottomNavigationScreens = listOf(
        YummyBitesScreens.HomeScreen.name,
        YummyBitesScreens.CartScreen.name,
        YummyBitesScreens.OrderScreen.name,
        YummyBitesScreens.ProfileScreen.name
    )
    val darkCyanColor = Color(0xFF008B8B)
    Scaffold(
        bottomBar = {
            BottomNavigation(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(2.dp))
                    .border(0.5.dp, color = Color.Cyan, shape = RoundedCornerShape(2.dp)),
                backgroundColor = Color.White
            ) {
                bottomNavigationScreens.forEach { screen ->
                    val isSelected = currentRoute(navController) == screen
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                imageVector = provideIcon(screen),
                                contentDescription = null,
                                tint = if (isSelected) Color.Cyan else darkCyanColor
                            )
                        },
                        label = {
                            Text(
                                text = provideName(screen),
                                color = if (isSelected) Color.Cyan else darkCyanColor
                            )
                        },
                        selected = isSelected,
                        onClick = {
                            navController.navigate(screen) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Content for the current screen
            NavHost(navController = navController, startDestination = YummyBitesScreens.HomeScreen.name) {
                composable(YummyBitesScreens.HomeScreen.name) {

                    YummyBitesHomeScreen(navController)
                }
                composable(YummyBitesScreens.OrderScreen.name) {
                      OrderScreen()
                }
                composable(YummyBitesScreens.CartScreen.name) {
                    CartScreen(navController)
                }
                composable(YummyBitesScreens.ProfileScreen.name) {
                    ProfileScreen(navController=navctl)
                }

                composable(
                    route = "${YummyBitesScreens.PaymentScreen.name}/{totalAmount}",
                    arguments = listOf(navArgument("totalAmount") { type = NavType.FloatType })
                ) { entry ->
                    val totalAmount = entry.arguments?.getFloat("totalAmount")?.toDouble() ?: 0.0
                    PaymentsScreen(totalAmount)
                }



            }
        }
    }
}

@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}


data class BottomNavigationItemInfo(
    val screen: YummyBitesScreens,
    val icon: ImageVector,
    val label: String
)


class BottomNavigationInfo(val screen: YummyBitesScreens,val icon:ImageVector,val name:String){

}

fun provideName(name: String): String {
    return when (name) {
        YummyBitesScreens.HomeScreen.name -> "Home"
        YummyBitesScreens.OrderScreen.name -> "Orders"
        YummyBitesScreens.CartScreen.name -> "Cart"
        YummyBitesScreens.ProfileScreen.name -> "Profile"
        else -> "Home"
    }
}

// Define the provideIcon function
fun provideIcon(name: String): ImageVector {
    return when (name) {
        YummyBitesScreens.HomeScreen.name -> Icons.Default.Home
        YummyBitesScreens.OrderScreen.name -> Icons.Default.List
        YummyBitesScreens.CartScreen.name -> Icons.Default.ShoppingCart
        YummyBitesScreens.ProfileScreen.name -> Icons.Default.Person
        else -> Icons.Default.Home
    }
}
