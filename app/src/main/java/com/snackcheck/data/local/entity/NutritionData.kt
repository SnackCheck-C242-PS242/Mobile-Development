package com.snackcheck.data.local.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NutritionData(
    val fat: Double = 0.0,
    val saturatedFat: Double = 0.0,
    val carbohydrates: Double = 0.0,
    val sugars: Double = 0.0,
    val fiber: Double = 0.0,
    val proteins: Double = 0.0,
    val sodium: Double = 0.0
) : Parcelable
