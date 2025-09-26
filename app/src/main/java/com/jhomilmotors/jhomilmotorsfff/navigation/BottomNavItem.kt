package com.jhomilmotors.jhomilmotorsfff.navigation

import com.jhomilmotors.jhomilmotorsfff.R

sealed class BottomNavItem(val title: String, val icon: Int, val route: String) {
    object Home : BottomNavItem("Inicio", R.drawable.ic_home, AppScreens.HomeScreen.route)
    object Cart : BottomNavItem("Carrito", R.drawable.ic_cart, AppScreens.CartScreen.route)
    object Orders : BottomNavItem("Pedidos", R.drawable.ic_orders, AppScreens.OrdersScreen.route)
    object Account : BottomNavItem("Cuenta", R.drawable.ic_account, AppScreens.ProfileScreen.route)
}