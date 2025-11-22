package com.jhomilmotors.jhomilmotorsfff.data.repository

import com.jhomilmotors.jhomilmotorsfff.data.model.SpringPage
import com.jhomilmotors.jhomilmotorsfff.data.model.category.CategoryResponse
import com.jhomilmotors.jhomilmotorsfff.data.model.product.ProductDetailsDto
import com.jhomilmotors.jhomilmotorsfff.data.model.product.ProductResponse
import com.jhomilmotors.jhomilmotorsfff.data.remote.ApiService
import retrofit2.Response
import javax.inject.Inject

class CategoryRepository  @Inject constructor(
    private val api: ApiService
) {

    suspend fun getCategories() : Response<List<CategoryResponse>>{
        return api.getCategories()
    }

    suspend fun getProductsByCategory(categoryId: Int): Response<SpringPage<ProductResponse>> {
        return api.getProductsByCategory(categoryId)
    }

    suspend fun getProductDetails(id : Long) : Response<ProductDetailsDto>{
        return api.getProductDetails(id)
    }
}