package com.example.fide_go.data.retrofit

import com.example.fide_go.data.model.User
import com.example.fide_go.data.model.dto.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {
    @GET("edentifica/users/get_by_email/{email}")// this is only for the email default of user
    suspend fun getByEmail(@Path("email") email: String): Response<User>

    @GET("edentifica/users/get_dto_by_email")
    suspend fun getDtoByEmail(@Query("email") email: String): Response<UserDto>

    @GET("edentifica/users/get_by_phone/{phonenumber}")// this is only for the phone default of user
    suspend fun getByPhone(@Path("phonenumber") phonenumber: String): Response<User>

    @GET("edentifica/users/get_dto_by_phone")
    suspend fun getDtoByPhone(@Query("phonenumber") phonenumber: String): Response<UserDto>

    @GET("edentifica/users/get_by_type_and_social_network/{type}/{socialname}")
    suspend fun getBySocialNetwork(@Path("type") type: String, @Path("socialname") socialname: String): Response<User>

    @GET("edentifica/users/get_dto_by_type_and_social_network/{type}")
    suspend fun getDtoBySocialNetwork(@Path("type") type: String, @Query("socialname") socialname: String): Response<UserDto>

    @PUT("edentifica/users/update")
    suspend fun updateUser(@Body user: User): Response<Boolean>
    @POST("edentifica/users/insert")
    suspend fun insertUser(@Body user: User): Response<User>

    @POST("edentifica/users/validation_one_call")
    suspend fun toDoCall(@Body user: User): Response<Boolean>

    @POST("edentifica/users/answer_math_challenge")
    suspend fun answerMathChallenge(
        @Query("answer") answer: Int,
        @Body user: User)
    : Response<Boolean>

}