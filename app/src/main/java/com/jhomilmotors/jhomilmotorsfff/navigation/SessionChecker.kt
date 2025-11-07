package com.jhomilmotors.jhomilmotorsfff.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.jhomilmotors.jhomilmotorsfff.ui.viewmodels.SessionViewModel

@Composable
fun SessionChecker(navController: NavHostController) {
    val viewModel: SessionViewModel = hiltViewModel()
    val isChecking by viewModel.isChecking.collectAsState()
    val sessionValid by viewModel.sessionValid.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.checkSession()
    }

    LaunchedEffect(sessionValid, isChecking) {
        if (!isChecking) {
            if (sessionValid) {
                navController.navigate(AppScreens.HomeScreen.route) {
                    popUpTo("session_checker") { inclusive = true }
                }
            } else {
                navController.navigate(AppScreens.Login.route) {
                    popUpTo("session_checker") { inclusive = true }
                }
            }
        }
    }

    if (isChecking) {
        Surface(Modifier.fillMaxSize()) {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}
