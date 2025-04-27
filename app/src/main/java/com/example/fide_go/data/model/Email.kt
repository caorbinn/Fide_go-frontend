package com.example.fide_go.data.model

import com.google.gson.annotations.SerializedName

data class Email(
    @SerializedName("id") var id:String?,
    @SerializedName("email") var email:String,
    @SerializedName("isVerified") var isVerified:Boolean?,
    @SerializedName("idProfileUser") var idProfileUser:String?
)
