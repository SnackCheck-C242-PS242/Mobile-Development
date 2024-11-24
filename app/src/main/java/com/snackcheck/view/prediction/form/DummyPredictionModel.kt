package com.snackcheck.view.prediction.form

import android.util.Log
import com.snackcheck.data.local.entity.SnackDetail

object DummyPredictionModel {
    fun predict(snackDetail: SnackDetail): String {
        Log.d("DummyPredictionModel", "Input: $snackDetail")

        // Hitung skor nutrisi berdasarkan komponen
        val fatScore = snackDetail.nutritionData.fat * 9
        val saturatedFatScore = snackDetail.nutritionData.saturatedFat * 9
        val carbScore = snackDetail.nutritionData.carbohydrates * 4
        val sugarScore = snackDetail.nutritionData.sugars * 4
        val fiberScore = snackDetail.nutritionData.fiber * -2
        val proteinScore = snackDetail.nutritionData.proteins * -4
        val sodiumScore = snackDetail.nutritionData.sodium * 0.1 // Sodium memiliki bobot kecil

        // Total skor nutrisi
        val totalScore = fatScore + saturatedFatScore + carbScore + sugarScore - fiberScore + proteinScore + sodiumScore

        // Tentukan kategori berdasarkan skor
        return when {
            totalScore > 500 -> "Unhealthy"
            totalScore > 300 -> "Moderate"
            else -> "Healthy"
        }
    }
}
