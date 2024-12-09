package com.snackcheck.data.remote.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SnackPredictResponse(
    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("result")
    val result: SnackPredictResult? = null,
) : Parcelable

@Parcelize
data class SnackPredictResult(
    @field:SerializedName("snackID")
    val snackId: String? = null,

    @field:SerializedName("snackName")
    val snackName: String? = null,

    @field:SerializedName("nutritions")
    val nutritions: Nutritions? = null,

    @field:SerializedName("health_status")
    val healthStatus: String? = null,

    @field:SerializedName("recommendation")
    val recommendation: String? = null,
) : Parcelable

@Parcelize
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
    val sodium: Double? = null,
) : Parcelable
