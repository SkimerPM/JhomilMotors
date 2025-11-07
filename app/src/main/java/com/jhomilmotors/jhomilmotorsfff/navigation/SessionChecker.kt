package com.jhomilmotors.jhomilmotorsfff.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.jhomilmotors.jhomilmotorsfff.data.model.RefreshRequest
import com.jhomilmotors.jhomilmotorsfff.data.remote.RetrofitClient
import com.jhomilmotors.jhomilmotorsfff.utils.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun SessionChecker(navController: NavHostController) {
    val context = LocalContext.current
    val checked = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val refreshToken = TokenManager.getRefreshToken(context)
        var success = false

        if (!refreshToken.isNullOrBlank()) {
            try {
                val api = RetrofitClient.getApiService(context)
                val response = withContext(Dispatchers.IO) {
                    api.refreshToken(
                        clientType = "mobile",
                        request = RefreshRequest( refreshToken)
                    )
                }

                if (response.isSuccessful && response.body() != null) {
                    val authResponse = response.body()!!
                    TokenManager.saveTokens(
                        context,
                        authResponse.accessToken,
                        authResponse.refreshToken
                    )
                    success = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
                success = false
            }
        }

        if (success) {
            navController.navigate(AppScreens.HomeScreen.route) {
                popUpTo("session_checker") { inclusive = true }
            }
        } else {
            TokenManager.clear(context)
            navController.navigate(AppScreens.Login.route) {
                popUpTo("session_checker") { inclusive = true }
            }
        }

        checked.value = true
    }

    if (!checked.value) {
        Surface(Modifier.fillMaxSize()) {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}
