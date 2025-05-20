package com.example.fide_go.data.retrofit

import com.example.fide_go.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {
    //CAMBIAR EDENTIFICA

    @GET("fide_go/users/get_by_email/{email}")// this is only for the email default of user
    suspend fun getByEmail(@Path("email") email: String): Response<User>


    @GET("fide_go/users/get_by_phone/{phonenumber}")// this is only for the phone default of user
    suspend fun getByPhone(@Path("phonenumber") phonenumber: String): Response<User>


    @GET("fide_go/users/get_by_type_and_social_network/{type}/{socialname}")
    suspend fun getBySocialNetwork(@Path("type") type: String, @Path("socialname") socialname: String): Response<User>


    @PUT("fide_go/users/update")
    suspend fun updateUser(@Body user: User): Response<Boolean>
    @POST("fide_go/users/insert")
    suspend fun insertUser(@Body user: User): Response<User>

    @POST("fide_go/users/validation_one_call")
    suspend fun toDoCall(@Body user: User): Response<Boolean>

    @POST("fide_go/users/answer_math_challenge")
    suspend fun answerMathChallenge(
        @Query("answer") answer: Int,
        @Body user: User)
    : Response<Boolean>

}