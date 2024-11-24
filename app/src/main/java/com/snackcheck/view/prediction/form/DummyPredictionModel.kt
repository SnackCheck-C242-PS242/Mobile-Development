package com.snackcheck.view.prediction.form

import com.snackcheck.data.local.entity.SnackDetail

object DummyPredictionModel {
    fun predict(snackDetail: SnackDetail): String {
        // Logika prediksi dummy berdasarkan kandungan nutrisi
        return when {
            snackDetail.nutritionData.fat > 15.0 -> "Unhealthy"
            snackDetail.nutritionData.carbohydrates > 20.0 -> "Moderate"
            else -> "Healthy"
        }
    }
}
