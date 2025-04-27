package com.example.fide_go.data.model

import com.google.gson.annotations.SerializedName

data class SocialNetwork(
    @SerializedName("id") var id: String?,
    @SerializedName("networkType") var networkType: NetworkType,
    @SerializedName("socialName") var socialName: String,
    @SerializedName("isVerified") var isVerified: Boolean?,
    @SerializedName("idProfileUser") var idProfileUser:String?
)
