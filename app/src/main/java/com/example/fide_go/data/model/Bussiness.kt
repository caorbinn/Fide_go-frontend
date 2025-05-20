package com.example.fide_go.data.model

import com.google.gson.annotations.SerializedName

data class Bussiness(
    @SerializedName("id") var id:String?,
    @SerializedName("email") var bussinessName:String,
    @SerializedName("bussinessDescription") var bussinessDescription:String?,
    @SerializedName("bussinessAddress") var bussinessAddress:String?,
    @SerializedName("offers") var offers:List<Offers>?
)
