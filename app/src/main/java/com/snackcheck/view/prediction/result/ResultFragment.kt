package com.snackcheck.view.prediction.result

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.snackcheck.R
import com.snackcheck.data.remote.model.Categories
import com.snackcheck.data.remote.model.SnackPredictResponse
import com.snackcheck.databinding.FragmentResultBinding


@Suppress("DEPRECATION")
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

        @Suppress("DEPRECATION") val snackPredictResponse = arguments?.getParcelable<SnackPredictResponse>("snackPredictResponse")
        Log.d("ResultFragment", "Snack Predict Response: $snackPredictResponse")
        val snackName = snackPredictResponse?.result?.snackName
        Log.d("ResultFragment", "Snack Name: $snackName")
        val snackDetail = snackPredictResponse?.result?.nutritions
        val healthStatus = snackPredictResponse?.result?.healthStatus
        val recommendation = snackPredictResponse?.result?.recommendation
        val categories = snackPredictResponse?.result?.categories

        snackDetail?.let {
            binding.tvSnackNamePlaceholder.text = snackName ?: getString(R.string.no_data)
            binding.tvFatAmount.text = it.fat.toString()
            binding.tvSaturatedFatAmount.text = it.saturatedFat.toString()
            binding.tvCarbohydratesAmount.text = it.carbohydrates.toString()
            binding.tvSugarsAmount.text = it.sugars.toString()
            binding.tvFiberAmount.text = it.fiber.toString()
            binding.tvProteinsAmount.text = it.protein.toString()
            binding.tvSodiumAmount.text = (it.sodium?.times(1000)).toString()
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

        categories?.let { setupCategories(it) }

        val builder: AlertDialog.Builder =
            MaterialAlertDialogBuilder(
                this@ResultFragment.requireContext(),
                R.style.MaterialAlertDialog_Rounded
            )
        builder.setView(R.layout.layout_category_hint)
        val dialog: AlertDialog = builder.create()

        binding.tbHint.setOnClickListener {
            dialog.show()
        }
    }

    private fun setupCategories(categories: Categories) {
        binding.apply {
            when (categories.fat) {
                "1" -> {
                    ivCategoryFat.setImageResource(R.drawable.ic_very_low)
                    ivCategoryFat.setColorFilter(resources.getColor(R.color.card_color_green))
                }
                "2" -> {
                    ivCategoryFat.setImageResource(R.drawable.ic_low)
                    ivCategoryFat.setColorFilter(resources.getColor(R.color.card_color_light_green))
                }
                "3" -> {
                    ivCategoryFat.setImageResource(R.drawable.ic_moderate)
                    ivCategoryFat.setColorFilter(resources.getColor(R.color.md_theme_onPrimaryContainer))
                }
                "4" -> {
                    ivCategoryFat.setImageResource(R.drawable.ic_high)
                    ivCategoryFat.setColorFilter(resources.getColor(R.color.card_color_light_red))
                }
                "5" -> {
                    ivCategoryFat.setImageResource(R.drawable.ic_very_high)
                    ivCategoryFat.setColorFilter(resources.getColor(R.color.card_color_red))
                }
                else -> {}
            }

            when (categories.saturatedFat) {
                "1" -> {
                    ivCategorySaturatedFat.setImageResource(R.drawable.ic_very_low)
                    ivCategorySaturatedFat.setColorFilter(resources.getColor(R.color.card_color_green))
                }
                "2" -> {
                    ivCategorySaturatedFat.setImageResource(R.drawable.ic_low)
                    ivCategorySaturatedFat.setColorFilter(resources.getColor(R.color.card_color_light_green))
                }
                "3" -> {
                    ivCategorySaturatedFat.setImageResource(R.drawable.ic_moderate)
                    ivCategorySaturatedFat.setColorFilter(resources.getColor(R.color.md_theme_onPrimaryContainer))
                }
                "4" -> {
                    ivCategorySaturatedFat.setImageResource(R.drawable.ic_high)
                    ivCategorySaturatedFat.setColorFilter(resources.getColor(R.color.card_color_light_red))
                }
                "5" -> {
                    ivCategorySaturatedFat.setImageResource(R.drawable.ic_very_high)
                    ivCategorySaturatedFat.setColorFilter(resources.getColor(R.color.card_color_red))
                }
                else -> {}
            }

            when (categories.sugars) {
                "1" -> {
                    ivCategorySugars.setImageResource(R.drawable.ic_very_low)
                    ivCategorySugars.setColorFilter(resources.getColor(R.color.card_color_green))
                }

                "2" -> {
                    ivCategorySugars.setImageResource(R.drawable.ic_low)
                    ivCategorySugars.setColorFilter(resources.getColor(R.color.card_color_light_green))
                }

                "3" -> {
                    ivCategorySugars.setImageResource(R.drawable.ic_moderate)
                    ivCategorySugars.setColorFilter(resources.getColor(R.color.md_theme_onPrimaryContainer))
                }

                "4" -> {
                    ivCategorySugars.setImageResource(R.drawable.ic_high)
                    ivCategorySugars.setColorFilter(resources.getColor(R.color.card_color_light_red))
                }

                "5" -> {
                    ivCategorySugars.setImageResource(R.drawable.ic_very_high)
                    ivCategorySugars.setColorFilter(resources.getColor(R.color.card_color_red))
                }
                else -> {}
            }

            when (categories.carbohydrates) {
                "1" -> {
                    ivCategoryCarbohydrates.setImageResource(R.drawable.ic_very_low)
                    ivCategoryCarbohydrates.setColorFilter(resources.getColor(R.color.card_color_red))
                }
                "2" -> {
                    ivCategoryCarbohydrates.setImageResource(R.drawable.ic_low)
                    ivCategoryCarbohydrates.setColorFilter(resources.getColor(R.color.card_color_light_red))
                }
                "3" -> {
                    ivCategoryCarbohydrates.setImageResource(R.drawable.ic_moderate)
                    ivCategoryCarbohydrates.setColorFilter(resources.getColor(R.color.md_theme_onPrimaryContainer))
                }
                "4" -> {
                    ivCategoryCarbohydrates.setImageResource(R.drawable.ic_high)
                    ivCategoryCarbohydrates.setColorFilter(resources.getColor(R.color.card_color_light_green))
                }
                "5" -> {
                    ivCategoryCarbohydrates.setImageResource(R.drawable.ic_very_high)
                    ivCategoryCarbohydrates.setColorFilter(resources.getColor(R.color.card_color_green))
                }
                else -> {}
            }

            when (categories.protein) {
                "1" -> {
                    ivCategoryProtein.setImageResource(R.drawable.ic_very_low)
                    ivCategoryProtein.setColorFilter(resources.getColor(R.color.card_color_red))
                }
                "2" -> {
                    ivCategoryProtein.setImageResource(R.drawable.ic_low)
                    ivCategoryProtein.setColorFilter(resources.getColor(R.color.card_color_light_red))
                }
                "3" -> {
                    ivCategoryProtein.setImageResource(R.drawable.ic_moderate)
                    ivCategoryProtein.setColorFilter(resources.getColor(R.color.md_theme_onPrimaryContainer))
                }
                "4" -> {
                    ivCategoryProtein.setImageResource(R.drawable.ic_high)
                    ivCategoryProtein.setColorFilter(resources.getColor(R.color.card_color_light_green))
                }
                "5" -> {
                    ivCategoryProtein.setImageResource(R.drawable.ic_very_high)
                    ivCategoryProtein.setColorFilter(resources.getColor(R.color.card_color_green))
                }
                else -> {}
            }

            when (categories.fiber) {
                "1" -> {
                    ivCategoryFiber.setImageResource(R.drawable.ic_very_low)
                    ivCategoryFiber.setColorFilter(resources.getColor(R.color.card_color_red))
                }
                "2" -> {
                    ivCategoryFiber.setImageResource(R.drawable.ic_low)
                    ivCategoryFiber.setColorFilter(resources.getColor(R.color.card_color_light_red))
                }
                "3" -> {
                    ivCategoryFiber.setImageResource(R.drawable.ic_moderate)
                    ivCategoryFiber.setColorFilter(resources.getColor(R.color.md_theme_onPrimaryContainer))
                }
                "4" -> {
                    ivCategoryFiber.setImageResource(R.drawable.ic_high)
                    ivCategoryFiber.setColorFilter(resources.getColor(R.color.card_color_light_green))
                }
                "5" -> {
                    ivCategoryFiber.setImageResource(R.drawable.ic_very_high)
                    ivCategoryFiber.setColorFilter(resources.getColor(R.color.card_color_green))
                }
                else -> {}
            }

            when (categories.sodium) {
                "1" -> {
                    ivCategorySodium.setImageResource(R.drawable.ic_very_low)
                    ivCategorySodium.setColorFilter(resources.getColor(R.color.card_color_green))
                }

                "2" -> {
                    ivCategorySodium.setImageResource(R.drawable.ic_low)
                    ivCategorySodium.setColorFilter(resources.getColor(R.color.card_color_light_green))
                }

                "3" -> {
                    ivCategorySodium.setImageResource(R.drawable.ic_moderate)
                    ivCategorySodium.setColorFilter(resources.getColor(R.color.md_theme_onPrimaryContainer))
                }

                "4" -> {
                    ivCategorySodium.setImageResource(R.drawable.ic_high)
                    ivCategorySodium.setColorFilter(resources.getColor(R.color.card_color_light_red))
                }

                "5" -> {
                    ivCategorySodium.setImageResource(R.drawable.ic_very_high)
                    ivCategorySodium.setColorFilter(resources.getColor(R.color.card_color_red))
                }

                else -> {}
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}