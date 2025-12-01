package com.jhomilmotors.jhomilmotorsfff.data.repository

import com.jhomilmotors.jhomilmotorsfff.data.model.ContentResponse
import com.jhomilmotors.jhomilmotorsfff.data.remote.ApiService
import retrofit2.Response
import javax.inject.Inject

class ContentRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun getContent(codigo: String): Response<ContentResponse> {
        return api.getContentByCode(codigo)
    }
}