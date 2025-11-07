package com.jhomilmotors.jhomilmotorsfff.data.repository

import com.jhomilmotors.jhomilmotorsfff.data.model.CustomerProfile
import com.jhomilmotors.jhomilmotorsfff.data.remote.ApiService
import retrofit2.Response
import javax.inject.Inject

class ProfileRepository @Inject constructor(private val api: ApiService) {

    suspend fun getProfile(): Response<CustomerProfile> = api.getCurrentUserProfile()

    suspend fun updateProfile(profile: CustomerProfile): Response<CustomerProfile> =
        api.updateUserProfile(profile)

}