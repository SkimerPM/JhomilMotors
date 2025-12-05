package com.jhomilmotors.jhomilmotorsfff

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.jhomilmotors.jhomilmotorsfff.data.model.UiState
import com.jhomilmotors.jhomilmotorsfff.ui.screens.register.RegisterContent
import com.jhomilmotors.jhomilmotorsfff.ui.theme.JhomilMotorsShopTheme
import org.junit.Rule
import org.junit.Test

class RegisterScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun autoFillRegistrationForm() {
        composeTestRule.setContent {
            JhomilMotorsShopTheme {
                var nombre by remember { mutableStateOf("") }
                var apellido by remember { mutableStateOf("") }
                var email by remember { mutableStateOf("") }
                var password by remember { mutableStateOf("") }
                var isChecked by remember { mutableStateOf(false) }

                RegisterContent(
                    nombre = nombre,
                    onNombreChange = { nombre = it },
                    apellido = apellido,
                    onApellidoChange = { apellido = it },
                    email = email,
                    onEmailChange = { email = it },
                    password = password,
                    onPasswordChange = { password = it },
                    isChecked = isChecked,
                    onCheckedChange = { isChecked = it },

                    registerState = UiState.Idle,
                    snackbarHostState = remember { SnackbarHostState() },

                    // Simulamos acciones
                    onRegisterClick = {},
                    onNavigateLogin = {},
                    onNavigateTyC = {},
                    onNavigatePrivacidad = {}
                )
            }
        }

        // 2. Rellenar Nombre
        composeTestRule.onNodeWithTag("input_nombre")
            .performScrollTo()
            .performTextInput("Felix")

        // 3. Rellenar Apellido
        composeTestRule.onNodeWithTag("input_apellido")
            .performScrollTo()
            .performTextInput("Diaz")

        // 4. Rellenar Email
        composeTestRule.onNodeWithTag("input_email")
            .performScrollTo()
            .performTextInput("felix.test@gmail.com")

        // 5. Rellenar Password
        composeTestRule.onNodeWithTag("input_password")
            .performScrollTo()
            .performTextInput("123456")

        // 6. Marcar Checkbox
        composeTestRule.onNodeWithTag("check_tyc")
            .performScrollTo()
            .performClick()

        Thread.sleep(1000)

        composeTestRule.onNodeWithTag("btn_register")
            .performScrollTo()
            .performClick()

        composeTestRule.onNodeWithText("Felix").assertExists()
        composeTestRule.onNodeWithText("Diaz").assertExists()

        Thread.sleep(3000)
    }
}