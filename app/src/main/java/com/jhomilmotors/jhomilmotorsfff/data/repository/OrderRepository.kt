package com.jhomilmotors.jhomilmotorsfff.data.repository

import com.jhomilmotors.jhomilmotorsfff.data.model.SpringPage
import com.jhomilmotors.jhomilmotorsfff.data.model.order.OrderResponse
import com.jhomilmotors.jhomilmotorsfff.data.remote.ApiService
import retrofit2.Response
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun getMyOrders(): Response<SpringPage<OrderResponse>> {
        return api.getMyOrders(0, 20)
    }
}