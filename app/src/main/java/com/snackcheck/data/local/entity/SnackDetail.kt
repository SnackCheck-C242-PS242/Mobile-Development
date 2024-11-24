package com.snackcheck.data.local.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SnackDetail(
    val name: String = "", // Nama snack
    val nutritionData: NutritionData = NutritionData() // Detail nutrisi
) : Parcelable
