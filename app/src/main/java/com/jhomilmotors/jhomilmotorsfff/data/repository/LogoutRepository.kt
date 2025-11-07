package com.jhomilmotors.jhomilmotorsfff.data.repository

import com.jhomilmotors.jhomilmotorsfff.data.remote.ApiService
import retrofit2.Response
import javax.inject.Inject

class LogoutRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun logout(): Response<Unit> = api.logout()
}