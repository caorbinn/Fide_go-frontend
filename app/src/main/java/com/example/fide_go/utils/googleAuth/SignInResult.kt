package com.example.fide_go.utils.googleAuth

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

data class UserData(
    val userId:String,
    val username:String?,
    val userEmail:String,
    val userPhone: String,
    val profilePictureUrl: String?
)