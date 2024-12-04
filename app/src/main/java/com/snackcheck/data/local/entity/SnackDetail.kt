package com.snackcheck.data.local.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SnackDetail(
    val snackId: String = "", // ID snack
    val snackName: String = "", // Nama snack
    val nutritions: NutritionData = NutritionData() // Detail nutrisi
) : Parcelable
