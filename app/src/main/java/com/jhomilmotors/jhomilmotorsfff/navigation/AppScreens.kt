package com.jhomilmotors.jhomilmotorsfff.navigation

/**
 * Usamos una 'sealed class' (clase sellada) para definir todas las rutas (pantallas)
 * de nuestra aplicación. Esto funciona como una agenda de contactos: nos da un lugar
 * central y seguro para referirnos a nuestras pantallas, evitando errores de tipeo.
 */
sealed class AppScreens(val route: String) {
    object HomeScreen : AppScreens("home_screen")
    object CartScreen : AppScreens("cart_screen")
    object OrdersScreen : AppScreens("orders_screen")
    object ProfileScreen : AppScreens("profile_screen")

    // --- Pantallas que no están en la barra de navegación ---
    object ConfirmationScreen : AppScreens("confirmation_screen")
    object CheckoutScreen : AppScreens("checkout_screen")
    object Login : AppScreens("login")

    object Register : AppScreens("register")
}