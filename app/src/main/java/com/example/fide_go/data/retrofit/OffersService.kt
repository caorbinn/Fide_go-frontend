package com.example.fide_go.data.retrofit

import com.example.fide_go.data.model.Bussiness
import com.example.fide_go.data.model.Offers
import com.example.fide_go.data.model.Phone
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface OffersService {
    @POST("fide_go/offers/insert")
    suspend fun insertOffers(@Body offers: Offers): Response<Boolean>

    @PUT("fide_go/offers/update")
    suspend fun updateOffers(@Body offers: Offers): Response<Boolean>

    @GET("fide_go/offers/get")
    suspend fun getOffers(@Query("id") id: String): Response<Offers>

    @DELETE("fide_go/offers/delete/{id}")
    suspend fun deleteOffers(@Path("id") id: String): Response<Boolean>

    @GET("fide_go/offers/byBusiness")
    suspend fun getOffersByBusiness(@Query("bussinessId") id: String): Response<List<Offers>>
}