package com.jhomilmotors.jhomilmotorsfff.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jhomilmotors.jhomilmotorsfff.ui.components.BottomNavigationBar
import com.jhomilmotors.jhomilmotorsfff.ui.screens.cart.CartScreen
import com.jhomilmotors.jhomilmotorsfff.ui.screens.confirmacion.ConfirmationScreen
import com.jhomilmotors.jhomilmotorsfff.ui.screens.historial.HistorialScreen
import com.jhomilmotors.jhomilmotorsfff.ui.screens.home.Principal
import com.jhomilmotors.jhomilmotorsfff.ui.screens.profile.ProfileScreen
import com.jhomilmotors.jhomilmotorsfff.ui.screens.login.Login
import com.jhomilmotors.jhomilmotorsfff.ui.screens.register.Register
import com.jhomilmotors.jhomilmotorsfff.ui.theme.JhomilMotorsShopTheme
import com.jhomilmotors.jhomilmotorsfff.utils.TokenManager
import com.jhomilmotors.jhomilmotorsfff.data.model.RefreshRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            val mainScreenRoutes = listOf(
                AppScreens.HomeScreen.route,
                AppScreens.CartScreen.route,
                AppScreens.OrdersScreen.route,
                AppScreens.ProfileScreen.route
            )

            if (currentRoute in mainScreenRoutes) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "session_checker",
            modifier = Modifier.padding(innerPadding)
        ) {
            // Chequeo de sesión persistente en esta pantalla especial
            composable("session_checker") {
                SessionChecker(navController)
            }
            composable(route = AppScreens.HomeScreen.route) {
                Principal(navController)
            }
            composable(route = AppScreens.CartScreen.route) {
                CartScreen(navController)
            }
            composable(route = AppScreens.OrdersScreen.route) {
                HistorialScreen(navController)
            }
            composable(route = AppScreens.ProfileScreen.route) {
                ProfileScreen(navController)
            }
            composable(route = AppScreens.ConfirmationScreen.route) {
                ConfirmationScreen(navController)
            }
            composable(route = AppScreens.Login.route) {
                Login(navController)
            }
            composable(route = AppScreens.Register.route) {
                Register(navController)
            }
        }
    }
}


@Preview(showSystemUi = false)
@Composable
fun AppNavigationPreview() {
    JhomilMotorsShopTheme {
        AppNavigation()
    }
}
