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

    @field:SerializedName("categories")
    val categories: Categories? = null,
) : Parcelable

@Parcelize
data class Nutritions(
    @field:SerializedName("fat")
    val fat: Double? = 0.0,

    @field:SerializedName("saturated_fat")
    val saturatedFat: Double? = 0.0,

    @field:SerializedName("carbohydrates")
    val carbohydrates: Double? = 0.0,

    @field:SerializedName("sugars")
    val sugars: Double? = 0.0,

    @field:SerializedName("fiber")
    val fiber: Double? = 0.0,

    @field:SerializedName("protein")
    val protein: Double? = 0.0,

    @field:SerializedName("sodium")
    val sodium: Double? = 0.0,
) : Parcelable

@Parcelize
data class Categories(
    @field:SerializedName("fat")
    val fat: String? = null,

    @field:SerializedName("saturated_fat")
    val saturatedFat: String? = null,

    @field:SerializedName("carbohydrates")
    val carbohydrates: String? = null,

    @field:SerializedName("sugars")
    val sugars: String? = null,

    @field:SerializedName("fiber")
    val fiber: String? = null,

    @field:SerializedName("proteins")
    val protein: String? = null,

    @field:SerializedName("sodium")
    val sodium: String? = null,
) : Parcelable
