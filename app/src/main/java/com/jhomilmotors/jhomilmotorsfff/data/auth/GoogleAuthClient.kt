package com.jhomilmotors.jhomilmotorsfff.data.auth

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleAuthClient @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val WEB_CLIENT_ID = "405542436515-b698rpem79qhd87ntoqdmgfntev4ub84.apps.googleusercontent.com"

    // 🟢 CAMBIO 1: Agregamos 'activityContext' como parámetro
    suspend fun signIn(activityContext: Context): String? {
        try {
            val credentialManager = CredentialManager.create(context)

            val hashedNonce = UUID.randomUUID().toString()

            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(WEB_CLIENT_ID)
                .setAutoSelectEnabled(false)
                .setNonce(hashedNonce) // <--- CAMBIA ESTO (Antes era null)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            // 🟢 CAMBIO 2: Usamos 'activityContext' aquí para lanzar la ventana visual
            val result = credentialManager.getCredential(
                request = request,
                context = activityContext
            )

            return handleSignIn(result)
        } catch (e: NoCredentialException) {
            Log.e("GoogleAuth", "No se encontraron credenciales: ${e.message}")
            return null
        } catch (e: GetCredentialException) {
            Log.e("GoogleAuth", "Error de Credential Manager: ${e.message}")
            return null
        } catch (e: Exception) {
            Log.e("GoogleAuth", "Error desconocido: ${e.message}")
            return null
        }
    }

    private fun handleSignIn(result: GetCredentialResponse): String? {
        val credential = result.credential
        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            try {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                return googleIdTokenCredential.idToken
            } catch (e: Exception) {
                Log.e("GoogleAuth", "Error parseando credencial", e)
            }
        }
        return null
    }
}