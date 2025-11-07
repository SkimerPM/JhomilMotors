package com.jhomilmotors.jhomilmotorsfff.data.repository

import com.jhomilmotors.jhomilmotorsfff.data.model.CustomerProfile
import com.jhomilmotors.jhomilmotorsfff.data.remote.ApiService
import retrofit2.http.GET
import retrofit2.http.Header

class ProfileRepository(private val api: ApiService) {
    suspend fun getProfile() = api.getCurrentUserProfile()
    suspend fun updateProfile(profile: CustomerProfile) = api.updateUserProfile(profile)
}