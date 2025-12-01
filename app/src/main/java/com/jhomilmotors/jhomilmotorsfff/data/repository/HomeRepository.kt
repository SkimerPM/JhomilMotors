package com.jhomilmotors.jhomilmotorsfff.data.repository

import com.jhomilmotors.jhomilmotorsfff.data.model.SpringPage
import com.jhomilmotors.jhomilmotorsfff.data.model.product.ProductOnSaleDTO
import com.jhomilmotors.jhomilmotorsfff.data.remote.ApiService
import retrofit2.Response
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val api: ApiService
) {

    // Products in Offer!
    suspend fun getProductOffer(page: Int) : Response<SpringPage<ProductOnSaleDTO>> {
        return api.getProductOffer(page = page, size = 10)
    }

}