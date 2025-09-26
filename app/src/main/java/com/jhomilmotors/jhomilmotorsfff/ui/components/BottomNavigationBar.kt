package com.jhomilmotors.jhomilmotorsfff.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.unit.dp
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
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 0.dp
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
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
                label = { Text(text = item.title) },
                colors = NavigationBarItemDefaults.colors(
                    // El color del contenido (ícono y texto) viene de 'onSurfaceVariant'
                    selectedIconColor = Color.White,
                    selectedTextColor = Color.White,

                    // Los no seleccionados son una versión semitransparente
                    unselectedIconColor = Color.White.copy(alpha = 0.6f),
                    unselectedTextColor = Color.White.copy(alpha = 0.6f),

                    // El indicador (la píldora de fondo) también usa un color derivado
                    indicatorColor = Color.White.copy(alpha = 0.15f)
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