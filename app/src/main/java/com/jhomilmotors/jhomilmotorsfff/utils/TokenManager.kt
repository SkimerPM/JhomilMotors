package com.jhomilmotors.jhomilmotorsfff.utils

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object TokenManager {
    private const val PREF_NAME = "secure_prefs"
    private const val ACCESS_TOKEN_KEY = "access_token"
    private const val REFRESH_TOKEN_KEY = "refresh_token"
    private fun getMasterKey(context: Context): MasterKey {
        return MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }

    private fun getPrefs(context: Context) =
        EncryptedSharedPreferences.create(
            context,
            PREF_NAME,
            getMasterKey(context),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    fun saveTokens(context: Context, accessToken: String, refreshToken: String?) {
        val prefs = getPrefs(context)
        val editor = prefs.edit()
        editor.putString(ACCESS_TOKEN_KEY, accessToken)
        if (!refreshToken.isNullOrBlank()) {
            editor.putString(REFRESH_TOKEN_KEY, refreshToken)
        }
        editor.apply()
    }

    fun getAccessToken(context: Context): String? =
        getPrefs(context).getString(ACCESS_TOKEN_KEY, null)

    fun getRefreshToken(context: Context): String? =
        getPrefs(context).getString(REFRESH_TOKEN_KEY, null)

    fun clear(context: Context) =
        getPrefs(context).edit().clear().apply()
}