package com.jhomilmotors.jhomilmotorsfff.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jhomilmotors.jhomilmotorsfff.ui.components.BottomNavigationBar
import com.jhomilmotors.jhomilmotorsfff.ui.screens.cart.CartScreen
import com.jhomilmotors.jhomilmotorsfff.ui.screens.confirmacion.ConfirmationScreen
import com.jhomilmotors.jhomilmotorsfff.ui.screens.historial.HistorialScreen
import com.jhomilmotors.jhomilmotorsfff.ui.screens.home.Principal
import com.jhomilmotors.jhomilmotorsfff.ui.screens.profile.ProfileScreen
import com.jhomilmotors.jhomilmotorsfff.ui.screens.login.Login
import com.jhomilmotors.jhomilmotorsfff.ui.screens.register.Register
import com.jhomilmotors.jhomilmotorsfff.ui.theme.JhomilMotorsShopTheme
import com.jhomilmotors.jhomilmotorsfff.ui.screens.DetailsProduct.DetailsProductoScreen
import com.jhomilmotors.jhomilmotorsfff.ui.screens.auth.VerificationPendingScreen
import com.jhomilmotors.jhomilmotorsfff.ui.screens.common.WebViewScreen

import com.jhomilmotors.jhomilmotorsfff.ui.screens.home.ProductListScreen
import com.jhomilmotors.jhomilmotorsfff.ui.screens.map.StoreMapScreen

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
            composable(
                route = AppScreens.VerificationPending.route,
                arguments = listOf(
                    navArgument("email") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val email = backStackEntry.arguments?.getString("email") ?: ""

                VerificationPendingScreen(
                    email = email,
                    onBackToLogin = {
                        // Al volver al login, limpiamos la pila para que no puedan volver atrás
                        navController.navigate(AppScreens.Login.route) {
                            popUpTo(AppScreens.VerificationPending.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(
                route = AppScreens.WebViewContent.route,
                arguments = listOf(navArgument("codigo") { type = NavType.StringType })
            ) { backStackEntry ->
                val codigo = backStackEntry.arguments?.getString("codigo") ?: ""
                WebViewScreen(
                    navController = navController,
                    codigo = codigo,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(
                route = AppScreens.ProductList.route, // "product_list/{categoryId}"
                arguments = listOf(
                    navArgument("categoryId") { type = NavType.IntType } // Decimos que es un Número
                )
            ) { backStackEntry ->
                // 1. Recuperamos el ID de la "mochila" (argumentos)
                val categoryId = backStackEntry.arguments?.getInt("categoryId") ?: 0
                // 2. Llamamos a la pantalla nueva pasándole el ID
                ProductListScreen(
                    categoryId = categoryId,
                    onBack = { navController.popBackStack() },
                    onNavigateToDetail = { productId ->

                        navController.navigate(AppScreens.ProductDetail.createRoute(productId.toInt().toLong()))
                    }
                )
            }
            composable(
                route = AppScreens.ProductDetail.route, // "product_detail/{productId}"
                arguments = listOf(
                    navArgument("productId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                // Capturamos el ID de la URL
                val productId = backStackEntry.arguments?.getInt("productId") ?: 0
                // El ViewModel se encargará de recibir el 'productId' (0) vía SavedStateHandle.
                DetailsProductoScreen(
                    productId = productId, // Pasamos el ID para el SavedStateHandle
                    onBack = { navController.popBackStack() }
                )
            }
            composable(AppScreens.StoreMap.route) {
                StoreMapScreen(onBack = { navController.popBackStack() })
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
