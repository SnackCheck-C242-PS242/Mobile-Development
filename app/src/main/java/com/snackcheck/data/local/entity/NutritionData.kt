package com.snackcheck.data.local.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NutritionData(
    val fat: Double = 0.0,
    val saturated_fat: Double = 0.0,
    val carbohydrates: Double = 0.0,
    val sugars: Double = 0.0,
    val fiber: Double = 0.0,
    val protein: Double = 0.0,
    val sodium: Double = 0.0
) : Parcelable
