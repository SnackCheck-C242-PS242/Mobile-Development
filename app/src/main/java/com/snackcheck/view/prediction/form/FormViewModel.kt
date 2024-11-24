package com.snackcheck.view.prediction.form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.snackcheck.data.local.entity.NutritionItem

class FormViewModel : ViewModel() {
    private val allNutrients = listOf("fat", "saturated fat", "carbohydrates", "sugars", "fiber", "proteins", "sodium")

    private val _nutrientStatus = MutableLiveData<Map<String, Int>>(
        allNutrients.associateWith { 0 } // Default: semua nutrisi belum dipilih
    )
    val nutrientStatus: LiveData<Map<String, Int>> get() = _nutrientStatus

    private val _isAddButtonVisible = MutableLiveData(true)
    val isAddButtonVisible: LiveData<Boolean> get() = _isAddButtonVisible

    private val _nutritionData = MutableLiveData<List<NutritionItem>>(listOf(NutritionItem()))
    val nutritionData: LiveData<List<NutritionItem>> get() = _nutritionData

    fun addEmptyNutritionItem() {
        val currentList = _nutritionData.value.orEmpty().toMutableList()
        val newItem = NutritionItem(name = "", amount = "")
        _nutritionData.value = currentList + newItem
        updateAddButtonVisibility()
    }

    fun removeLastNutritionItem() {
        val currentList = _nutritionData.value.orEmpty().toMutableList()

        if (currentList.size > 1) {
            val lastItem = currentList.last()

            // Kembalikan status nutrisi dari item terakhir
            updateNutrientStatus(null, lastItem.name)

            currentList.removeAt(currentList.size - 1)
            _nutritionData.value = currentList
            updateAddButtonVisibility()
        }
    }

    fun updateNutrientStatus(selected: String?, previous: String?) {
        val currentStatus = _nutrientStatus.value.orEmpty().toMutableMap()

        // Hapus status pilihan sebelumnya jika ada
        if (!previous.isNullOrEmpty()) {
            currentStatus[previous] = 0
        }

        // Tandai status nutrisi yang baru dipilih
        if (!selected.isNullOrEmpty()) {
            currentStatus[selected] = 1
        }

        _nutrientStatus.value = currentStatus
        updateAddButtonVisibility()
    }

    private fun updateAddButtonVisibility() {
        val currentStatus = _nutrientStatus.value ?: emptyMap()
        val currentForms = _nutritionData.value?.size ?: 0

        // Tombol tambah hanya terlihat jika ada nutrisi yang belum dipilih dan jumlah form kurang dari total nutrisi
        _isAddButtonVisible.value = currentStatus.values.contains(0) && currentForms < allNutrients.size
    }
}
