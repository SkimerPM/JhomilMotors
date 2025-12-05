package com.jhomilmotors.jhomilmotorsfff.data.repository

import com.jhomilmotors.jhomilmotorsfff.data.model.SearchResult
import com.jhomilmotors.jhomilmotorsfff.data.remote.ApiService
import retrofit2.Response
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun search(query: String): Response<List<SearchResult>> {
        return api.searchProducts(query)
    }
}