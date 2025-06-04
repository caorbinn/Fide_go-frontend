package com.example.fide_go.data.model

import com.google.gson.annotations.SerializedName

data class Offers(
    @SerializedName("id") var id:String?,
    @SerializedName("title") var title:String,
    @SerializedName("description") var description:String?,
    @SerializedName("termsAndConditions") var termsAndConditions:String?,
    @SerializedName("points") var points:Int?,
    @SerializedName("bussinessId") var bussinessId:String?,
    @SerializedName("urlImageOffer") var urlImageOffer:String?,
    @SerializedName("redeemCode") var redeemCode: String?
)