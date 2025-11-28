package com.jhomilmotors.jhomilmotorsfff.data.auth

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import javax.inject.Inject

class GoogleAuthClient @Inject constructor(
    private val context: Context
) {
    // ⚠️ IMPORTANTE: Este ID debe ser el de "Web Client" en tu Google Cloud Console,
    // NO el de Android. Es el mismo que usas en tu Backend Spring Boot.
    private val WEB_CLIENT_ID = "405542436515-b698rpem79qhd87ntoqdmgfntev4ub84.apps.googleusercontent.com"

    suspend fun signIn(): String? {
        try {
            val credentialManager = CredentialManager.create(context)

            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(WEB_CLIENT_ID)
                .setAutoSelectEnabled(true) // Intenta loguear automático si ya hay sesión
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(
                request = request,
                context = context
            )

            return handleSignIn(result)
        } catch (e: Exception) {
            Log.e("GoogleAuth", "Error en Google Sign In: ${e.message}")
            return null
        }
    }

    private fun handleSignIn(result: GetCredentialResponse): String? {
        val credential = result.credential
        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            try {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                // Retornamos el ID TOKEN (esto es lo que enviaremos al Backend)
                return googleIdTokenCredential.idToken
            } catch (e: Exception) {
                Log.e("GoogleAuth", "Error parseando credencial", e)
            }
        }
        return null
    }
}