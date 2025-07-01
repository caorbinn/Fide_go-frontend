package com.example.fide_go.data.retrofit

import com.example.fide_go.data.model.QrPurchase
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface PurchaseService {
    @POST("fide_go/purchases/scan/{userId}")
    suspend fun registerPurchase(@Path("userId") userId: String, @Body purchase: QrPurchase): Response<QrPurchase>
}