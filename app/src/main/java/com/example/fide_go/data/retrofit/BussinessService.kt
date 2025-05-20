package com.example.fide_go.data.retrofit

import com.example.fide_go.data.model.Bussiness
import com.example.fide_go.data.model.Email
import com.example.fide_go.data.model.Phone
import com.example.fide_go.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface BussinessService {
    @POST("fide_go/bussiness/insert")
    suspend fun insertBussiness(@Body bussiness: Bussiness): Response<Boolean>

    @PUT("fide_go/bussiness/update")
    suspend fun updateBussiness(@Body bussiness: Bussiness): Response<Boolean>

    @GET("fide_go/bussiness/get")
    suspend fun getBussiness(@Query("id") id: String): Response<Bussiness>

    @GET("fide_go/bussiness/getall")
    suspend fun getAllBussiness(): Response<List<Bussiness>>


    @DELETE("fide_go/bussiness/delete/{id}")
    suspend fun deleteBussiness(@Path("id") id: String): Response<Boolean>
}