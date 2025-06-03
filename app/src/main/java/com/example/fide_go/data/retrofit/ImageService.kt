package com.example.fide_go.data.retrofit

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ImageService {
    @Multipart
    @POST("api/images/upload")
    suspend fun uploadImage(@Part file: MultipartBody.Part): Response<String>
}