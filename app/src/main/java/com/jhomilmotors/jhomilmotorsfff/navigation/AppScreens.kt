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

    object VerificationPending : AppScreens("verification_pending/{email}") {
        fun createRoute(email: String) = "verification_pending/$email"
    }

    object WebViewContent : AppScreens("webview_content/{codigo}") {
        fun createRoute(codigo: String) = "webview_content/$codigo"
    }
    //modificadoa qui
    object ProductList : AppScreens("product_list/{categoryId}/{categoryName}") {
        fun createRoute(categoryId: Int, categoryName: String) = "product_list/$categoryId/$categoryName"
    }
    object ProductDetail : AppScreens("product_detail/{productId}") {
        fun createRoute(id: Long) = "product_detail/$id"
    }
    object StoreMap : AppScreens("store_map")
    object SearchScreen : AppScreens("search_screen")
}