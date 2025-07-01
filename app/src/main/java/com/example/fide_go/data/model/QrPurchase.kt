package com.example.fide_go.data.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class QrPurchase(
    @SerializedName("id") var id: String?,
    @SerializedName("amount") var amount: Double,
    @SerializedName("business") var business: String,
    @SerializedName("date") var date: LocalDate
)