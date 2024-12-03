package com.snackcheck.view.prediction.result

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.snackcheck.R
import com.snackcheck.data.local.entity.SnackDetail
import com.snackcheck.databinding.FragmentResultBinding


class ResultFragment : Fragment() {
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ambil data dari arguments
        val snackDetail = arguments?.getParcelable<SnackDetail>("snackDetail")
        val predictionResult = arguments?.getString("predictionResult")
        Log.d("FormFragment", "SnackDetail: $snackDetail")
        Log.d("FormFragment", "Hasil prediksi: $predictionResult")

        snackDetail?.let {
            binding.tvSnackNamePlaceholder.text = it.name
            binding.tvFatAmount.text = it.nutritionData.fat.toString()
            binding.tvSaturatedFatAmount.text = it.nutritionData.saturatedFat.toString()
            binding.tvCarbohydratesAmount.text = it.nutritionData.carbohydrates.toString()
            binding.tvSugarsAmount.text = it.nutritionData.sugars.toString()
            binding.tvFiberAmount.text = it.nutritionData.fiber.toString()
            binding.tvProteinsAmount.text = it.nutritionData.proteins.toString()
            binding.tvSodiumAmount.text = it.nutritionData.sodium.toString()
        } ?: run {
            binding.tvSnackNamePlaceholder.text = getString(R.string.no_data)
        }

        binding.tvPredictionResultPlaceholder.text = predictionResult ?: getString(R.string.no_prediction)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}