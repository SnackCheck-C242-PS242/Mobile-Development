package com.snackcheck.view.prediction.form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snackcheck.data.ResultState
import com.snackcheck.data.UserRepository
import com.snackcheck.data.local.entity.NutritionData
import com.snackcheck.data.local.entity.NutritionItem
import com.snackcheck.data.local.entity.SnackDetail
import com.snackcheck.data.remote.model.SnackPredictResponse
import kotlinx.coroutines.launch

class FormViewModel(private val repository: UserRepository) : ViewModel() {

    private val allNutrients = listOf("Fat", "Saturated Fat", "Carbohydrates", "Sugars", "Fiber", "Proteins", "Salt")

    private val _nutrientStatus = MutableLiveData(
        allNutrients.associateWith { 0 }
    )
    val nutrientStatus: LiveData<Map<String, Int>> get() = _nutrientStatus

    private val _isAddButtonVisible = MutableLiveData(true)
    val isAddButtonVisible: LiveData<Boolean> get() = _isAddButtonVisible

    private val _nutritionData = MutableLiveData(listOf(NutritionItem()))
    val nutritionData: LiveData<List<NutritionItem>> get() = _nutritionData

    private val _responseResult = MutableLiveData<ResultState<SnackPredictResponse>>()
    val responseResult = _responseResult

    fun predictSnack(snackDetail: SnackDetail) {
        viewModelScope.launch {
            try {
                _responseResult.value = ResultState.Loading
                val response = repository.predictSnack(snackDetail)
                if (response.status=="success") {
                    _responseResult.value = ResultState.Success(response)
                }
            } catch (e: Exception) {
                _responseResult.value = ResultState.Error(e.message.toString())
            }
        }
    }

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
            sodium = ((formData["Salt"] ?: 0.0) * 1/1000)
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
