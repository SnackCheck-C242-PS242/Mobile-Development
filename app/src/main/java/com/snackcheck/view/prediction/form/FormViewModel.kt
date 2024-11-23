package com.snackcheck.view.prediction.form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.snackcheck.data.local.entity.NutritionItem

class FormViewModel : ViewModel() {
    private val _nutritionData = MutableLiveData(listOf(NutritionItem()))
    val nutritionData: LiveData<List<NutritionItem>> get() = _nutritionData

    fun addEmptyNutritionItem() {
        val currentList = _nutritionData.value.orEmpty().toMutableList()
        _nutritionData.value = currentList + NutritionItem()
    }

    fun removeLastNutritionItem() {
        val currentList = _nutritionData.value.orEmpty().toMutableList()
        if (currentList.size > 1) { // Pastikan ada lebih dari satu elemen
            currentList.removeAt(currentList.size - 1) // Hapus elemen terakhir
            _nutritionData.value = currentList
        }
    }
}