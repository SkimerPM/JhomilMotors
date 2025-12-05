package com.jhomilmotors.jhomilmotorsfff

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.jhomilmotors.jhomilmotorsfff.data.model.CustomerProfile
import com.jhomilmotors.jhomilmotorsfff.data.model.UiState
import com.jhomilmotors.jhomilmotorsfff.ui.screens.profile.ProfileContent
import com.jhomilmotors.jhomilmotorsfff.ui.theme.JhomilMotorsShopTheme
import org.junit.Rule
import org.junit.Test

class ProfileScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun editProfileFlow() {
        composeTestRule.setContent {
            JhomilMotorsShopTheme {
                // Estados simulados
                var name by remember { mutableStateOf("Juan") }
                var lastname by remember { mutableStateOf("Pérez") }
                var phone by remember { mutableStateOf("999000111") }
                var address by remember { mutableStateOf("Av. Larco 123") }
                var isEditMode by remember { mutableStateOf(false) }

                ProfileContent(
                    name = name, onNameChange = { name = it },
                    lastname = lastname, onLastnameChange = { lastname = it },
                    email = "juan.perez@gmail.com",
                    phoneNumber = phone, onPhoneNumberChange = { phone = it },
                    address = address, onAddressChange = { address = it },

                    // Simulamos que la carga fue exitosa
                    profileState = UiState.Success(CustomerProfile( "Juan", "Pérez", "juan@gmail.com", "999...", "Av...")),
                    updateState = UiState.Idle,
                    logoutState = UiState.Idle,
                    isEditMode = isEditMode,
                    onEditModeChange = { isEditMode = it },

                    // Resto de callbacks vacíos para el test visual
                    snackbarHostState = remember { SnackbarHostState() },
                    isSessionValid = true,
                    isCheckingSession = false,
                    onBackClick = {}, onSaveProfile = { isEditMode = false }, // Al guardar, salimos de modo edit
                    onCancelEdit = { isEditMode = false },
                    onLogout = {}, onRetryLoad = {}, onCallSupport = {}, onWhatsappSupport = {}, onEmailSupport = {}
                )
            }
        }

        // --- INICIO DEL TEST ---

        // 1. Verificar que los campos no son editables al inicio
        // (Intentamos escribir y no debería cambiar, pero visualmente comprobamos que el botón editar existe)
        composeTestRule.onNodeWithTag("btn_edit_profile").assertExists()
        composeTestRule.onNodeWithTag("btn_save_profile").assertDoesNotExist()

        // 2. Clic en "Editar Perfil"
        composeTestRule.onNodeWithTag("btn_edit_profile").performClick()

        // 3. Verificar que cambió a modo edición
        composeTestRule.onNodeWithTag("btn_save_profile").assertExists()

        // 4. Cambiar el Teléfono
        composeTestRule.onNodeWithTag("input_phone")
            .performScrollTo()
            .performTextClearance() // Borrar actual
        composeTestRule.onNodeWithTag("input_phone")
            .performTextInput("987654321")

        // 5. Cambiar la Dirección
        composeTestRule.onNodeWithTag("input_address")
            .performScrollTo()
            .performTextClearance()
        composeTestRule.onNodeWithTag("input_address")
            .performTextInput("Calle Real 456")

        // 7. Guardar Cambios
        composeTestRule.onNodeWithTag("btn_save_profile")
            .performScrollTo()
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule.mainClock.advanceTimeBy(3000)

        composeTestRule.waitForIdle()

        // 8. Verificar que el botón de editar volvió
        composeTestRule.onNodeWithTag("btn_edit_profile")
            .assertExists()
            .performScrollTo() // Asegurar que esté en pantalla

        // 9. Verificar que los datos se quedaron "guardados" visualmente
        composeTestRule.onNodeWithTag("input_address").assertTextContains("Calle Real 456")
    }
}