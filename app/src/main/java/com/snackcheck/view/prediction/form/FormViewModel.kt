package com.snackcheck.view.prediction.form

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.snackcheck.R
import com.snackcheck.data.ResultState
import com.snackcheck.data.UserRepository
import com.snackcheck.data.local.entity.NutritionData
import com.snackcheck.data.local.entity.NutritionItem
import com.snackcheck.data.local.entity.SnackDetail
import com.snackcheck.data.remote.model.SnackPredictResponse
import kotlinx.coroutines.launch

class FormViewModel(application: Application, private val repository: UserRepository) : AndroidViewModel(application) {

    private val context = application.applicationContext

    private val allNutrients = listOf(
        context.getString(R.string.fat),
        context.getString(R.string.saturated_fat),
        context.getString(R.string.carbohydrates),
        context.getString(R.string.sugars),
        context.getString(R.string.fiber),
        context.getString(R.string.proteins),
        context.getString(R.string.sodium)
    )

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
            fat = formData[context.getString(R.string.fat)] ?: 0.0,
            saturated_fat = formData[context.getString(R.string.saturated_fat)] ?: 0.0,
            carbohydrates = formData[context.getString(R.string.carbohydrates)] ?: 0.0,
            sugars = formData[context.getString(R.string.sugars)] ?: 0.0,
            fiber = formData[context.getString(R.string.fiber)] ?: 0.0,
            protein = formData[context.getString(R.string.proteins)] ?: 0.0,
            sodium = ((formData[context.getString(R.string.sodium)] ?: 0.0) * 1 / 1000)
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
