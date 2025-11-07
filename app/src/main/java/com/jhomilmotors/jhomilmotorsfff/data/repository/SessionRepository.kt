// data/repository/SessionRepository.kt
package com.jhomilmotors.jhomilmotorsfff.data.repository

import com.jhomilmotors.jhomilmotorsfff.data.model.RefreshRequest
import com.jhomilmotors.jhomilmotorsfff.data.remote.ApiService
import javax.inject.Inject

class SessionRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun refreshToken(refreshToken: String) =
        api.refreshToken(
            clientType = "mobile",
            request = RefreshRequest(refreshRequest = refreshToken)
        )
}
