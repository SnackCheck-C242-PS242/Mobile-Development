package com.snackcheck.data.remote.model

import com.google.gson.annotations.SerializedName

data class SnackPredictResponse(
    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("result")
    val data: SnackPredictResult? = null
)

data class SnackPredictResult(
    @field:SerializedName("snackID")
    val snackId: String? = null,

    @field:SerializedName("snackName")
    val snackName: String? = null,

    @field:SerializedName("nutritionData")
    val nutritions: Nutritions? = null,

    @field:SerializedName("health_status")
    val healthStatus: String? = null,

    @field:SerializedName("recommendation")
    val recommendation: String? = null
)

data class Nutritions(
    @field:SerializedName("fat")
    val fat: Double? = null,

    @field:SerializedName("saturatedFat")
    val saturatedFat: Double? = null,

    @field:SerializedName("carbohydrates")
    val carbohydrates: Double? = null,

    @field:SerializedName("sugars")
    val sugars: Double? = null,

    @field:SerializedName("fiber")
    val fiber: Double? = null,

    @field:SerializedName("proteins")
    val proteins: Double? = null,

    @field:SerializedName("sodium")
    val sodium: Double? = null
)
