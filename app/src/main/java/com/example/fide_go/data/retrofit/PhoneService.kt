package com.example.fide_go.data.retrofit

import com.example.fide_go.data.model.Email
import com.example.fide_go.data.model.Phone
import com.example.fide_go.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PhoneService {
    //CAMBIAR EDENTIFICA

    @PUT("fide_go/phones/update")
    suspend fun updatePhone(@Body phone: Phone): Response<Boolean>

    @GET("fide_go/phones/get/{idprofile}")
    suspend fun listPhonesUser(@Path("idprofile") idprofile: String): Response<Set<Phone>>

    @GET("fide_go/phones/get")
    suspend fun getPhone(@Query("id") id: String): Response<Phone>

    @DELETE("fide_go/phones/delete/{id}")
    suspend fun deletePhone(@Path("id") id: String): Response<Boolean>
}