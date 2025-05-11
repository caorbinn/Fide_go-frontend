package com.example.fide_go.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") var id:String?,
    @SerializedName("username") var username:String?,
    @SerializedName("phone")var phone:Phone,
    @SerializedName("email")var email:Email,
    @SerializedName("profile")var profile:Profile?


)
