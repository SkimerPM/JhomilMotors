package com.jhomilmotors.jhomilmotorsfff.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jhomilmotors.jhomilmotorsfff.navigation.BottomNavItem
import com.jhomilmotors.jhomilmotorsfff.ui.theme.JhomilMotorsShopTheme

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Cart,
        BottomNavItem.Orders,
        BottomNavItem.Account
    )

    NavigationBar(
        containerColor = Color(0xFF1D428A)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) { saveState = true }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title, tint = Color.White) },
                label = { Text(text = item.title) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    unselectedIconColor = Color.White.copy(alpha = 0.6f),
                    unselectedTextColor = Color.White.copy(alpha = 0.6f),
                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                )
            )
        }
    }
}

@Preview
@Composable
fun BottomNavigationBarPreview() {
    // 1. Usamos nuestro tema para que la preview use los colores y fuentes correctos
    JhomilMotorsShopTheme {
        // 2. Como el componente necesita un NavController, creamos uno "falso" solo para la preview
        val navController = rememberNavController()

        // 3. Llamamos a nuestro componente para que se dibuje en la vista previa
        BottomNavigationBar(navController = navController)
    }
}