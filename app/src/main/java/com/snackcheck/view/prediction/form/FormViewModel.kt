package com.snackcheck.view.prediction.form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.snackcheck.data.UserRepository
import com.snackcheck.data.local.entity.NutritionData
import com.snackcheck.data.local.entity.NutritionItem
import com.snackcheck.data.local.entity.SnackDetail

class FormViewModel(private val repository: UserRepository) : ViewModel() {

    private val allNutrients = listOf("Fat", "Saturated Fat", "Carbohydrates", "Sugars", "Fiber", "Proteins", "Sodium")

    private val _nutrientStatus = MutableLiveData(
        allNutrients.associateWith { 0 }
    )
    val nutrientStatus: LiveData<Map<String, Int>> get() = _nutrientStatus

    private val _isAddButtonVisible = MutableLiveData(true)
    val isAddButtonVisible: LiveData<Boolean> get() = _isAddButtonVisible

    private val _nutritionData = MutableLiveData(listOf(NutritionItem()))
    val nutritionData: LiveData<List<NutritionItem>> get() = _nutritionData

    /*
    fun addSnack(snackDetail: SnackDetail) {
        if (snackDetail.snackName.isEmpty()) {
            throw IllegalArgumentException("Snack name cannot be empty")
        }

        val currentSnacks = _snacks.value.orEmpty().toMutableList()

        if (currentSnacks.any { it.snackName == snackDetail.snackName }) {
            throw IllegalArgumentException("Snack with the same name already exists")
        }

        currentSnacks.add(snackDetail)
        _snacks.value = currentSnacks
        _nutritionData.value = listOf(NutritionItem())
        _nutrientStatus.value = allNutrients.associateWith { 0 }
        updateAddButtonVisibility()
    }*/

    fun getSnackDetailFromInput(snackName: String): SnackDetail {
        val formData = _nutritionData.value.orEmpty().associate { item ->
            item.name to (item.amount.toDoubleOrNull() ?: 0.0)
        }

        val nutritionData = NutritionData(
            fat = formData["Fat"] ?: 0.0,
            saturatedFat = formData["Saturated Fat"] ?: 0.0,
            carbohydrates = formData["Carbohydrates"] ?: 0.0,
            sugars = formData["Sugars"] ?: 0.0,
            fiber = formData["Fiber"] ?: 0.0,
            proteins = formData["Proteins"] ?: 0.0,
            sodium = ((formData["Sodium"] ?: 0.0) * 1/1000)
        )

        return SnackDetail(snackName = snackName, nutritions = nutritionData)
    }

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
