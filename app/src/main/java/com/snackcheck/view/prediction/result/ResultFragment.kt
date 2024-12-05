package com.snackcheck.view.prediction.result

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.snackcheck.R
import com.snackcheck.data.local.entity.SnackDetail
import com.snackcheck.data.remote.model.SnackPredictResponse
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

        val snackPredictResponse = arguments?.getParcelable<SnackPredictResponse>("snackPredictResponse")
        Log.d("ResultFragment", "Snack Predict Response: $snackPredictResponse")
        val snackName = snackPredictResponse?.result?.snackName
        Log.d("ResultFragment", "Snack Name: $snackName")
        val snackDetail = snackPredictResponse?.result?.nutritions
        val healthStatus = snackPredictResponse?.result?.healthStatus
        val recommendation = snackPredictResponse?.result?.recommendation

        snackDetail?.let {
            binding.tvSnackNamePlaceholder.text = snackName ?: getString(R.string.no_data)
            binding.tvFatAmount.text = it.fat.toString()
            binding.tvSaturatedFatAmount.text = it.saturatedFat.toString()
            binding.tvCarbohydratesAmount.text = it.carbohydrates.toString()
            binding.tvSugarsAmount.text = it.sugars.toString()
            binding.tvFiberAmount.text = it.fiber.toString()
            binding.tvProteinsAmount.text = it.proteins.toString()
            binding.tvSodiumAmount.text = it.sodium.toString()
        } ?: run {
            binding.tvSnackNamePlaceholder.text = getString(R.string.no_data)
        }

        binding.tvPredictionResultPlaceholder.setTextColor(resources.getColor(R.color.md_theme_inversePrimary_highContrast))
        if (healthStatus == "Healthy") {
            binding.cvPredictionResult.setCardBackgroundColor(resources.getColor(R.color.card_color_green))
        } else {
            binding.cvPredictionResult.setCardBackgroundColor(resources.getColor(R.color.card_color_red))
        }
        binding.tvPredictionResultPlaceholder.text = healthStatus.toString()
        binding.tvRecommendationPlaceholder.text = recommendation.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}