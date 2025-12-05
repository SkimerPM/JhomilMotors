package com.jhomilmotors.jhomilmotorsfff.data.repository

import com.jhomilmotors.jhomilmotorsfff.data.remote.ApiService
import retrofit2.Response
import javax.inject.Inject

class AiRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun askToAssistant(question: String): Response<Map<String, String>> {
        return api.askAI(question)
    }
}