package com.jhomilmotors.jhomilmotorsfff.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
import com.jhomilmotors.jhomilmotorsfff.ui.theme.JhomilMotorsShopTheme

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            // ... lógica para decidir si se muestra la barra ...
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            // --- Define aquí la lista de tus 4 rutas principales ---
            val mainScreenRoutes = listOf(
                AppScreens.HomeScreen.route,
                AppScreens.CartScreen.route,
                AppScreens.OrdersScreen.route,
                AppScreens.ProfileScreen.route
            )

            // --- El "guardia de seguridad" ---
            // Si la ruta en la que está el usuario es una de las de la lista,
            // entonces muestra la barra de navegación.
            if (currentRoute in mainScreenRoutes) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppScreens.HomeScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Aquí defines todas las pantallas que tu app puede mostrar.
            composable(route = AppScreens.HomeScreen.route) {
                Principal()
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
        }
    }
}

@Preview(showSystemUi = false) // showSystemUi = true es genial para ver la app completa
@Composable
fun AppNavigationPreview() {
    // Usamos nuestro tema para que la preview se vea con los estilos correctos
    JhomilMotorsShopTheme {
        AppNavigation()
    }
}