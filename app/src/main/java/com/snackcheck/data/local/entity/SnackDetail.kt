package com.snackcheck.data.local.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SnackDetail(
    val nutritions: NutritionData = NutritionData(), // Detail nutrisi
    val snackName: String = "" // Nama snack
) : Parcelable
