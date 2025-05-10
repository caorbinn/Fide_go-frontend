package com.example.fide_go.data.retrofit

import com.example.fide_go.data.model.Email
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface EmailService {

    //CAMBIAR EDENTIFICA
    @PUT("edentifica/emails/update")
    suspend fun updateEmail(@Body email: Email): Response<Boolean>

    @GET("edentifica/emails/get/{idprofile}")
    suspend fun listEmailsUser(@Path("idprofile") idprofile: String): Response<Set<Email>>

    @GET("edentifica/emails/get")
    suspend fun getEmail(@Query("id") id: String): Response<Email>

    @DELETE("edentifica/emails/delete/{id}")
    suspend fun deleteEmail(@Path("id") id: String): Response<Boolean>
}