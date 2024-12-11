package com.snackcheck.view.history.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.snackcheck.R
import com.snackcheck.data.ResultState
import com.snackcheck.data.pref.UserPreference
import com.snackcheck.data.pref.dataStore
import com.snackcheck.data.remote.model.Categories
import com.snackcheck.data.remote.model.HistoryData
import com.snackcheck.databinding.FragmentHistoryDetailBinding
import com.snackcheck.view.ViewModelFactory

@Suppress("DEPRECATION")
class HistoryDetailFragment : Fragment() {
    private var _binding: FragmentHistoryDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var pref: UserPreference
    private lateinit var factory: ViewModelFactory
    private val viewModel by viewModels<HistoryDetailViewModel> { factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHistoryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = UserPreference.getInstance(requireContext().dataStore)
        factory = ViewModelFactory.getInstance(requireContext(), pref)

        val snackId = arguments?.getString("snackId") ?: ""

        viewModel.getHistoryDetail(snackId)
        setupObserver()

        setupAction()
    }

    private fun setupAction() {
        binding.apply {
            btnReloadHistory.setOnClickListener {
                val snackId = arguments?.getString("snackId") ?: ""
                viewModel.getHistoryDetail(snackId)
                setupObserver()
            }

            btnBack.setOnClickListener {
                requireActivity().onBackPressed()
            }

            val builder: AlertDialog.Builder =
                MaterialAlertDialogBuilder(
                    this@HistoryDetailFragment.requireContext(),
                    R.style.MaterialAlertDialog_Rounded
                )
            builder.setView(R.layout.layout_category_hint)
            val dialog: AlertDialog = builder.create()

            tbHint.setOnClickListener {
                dialog.show()
            }
        }
    }

    private fun setupUI(historyData: HistoryData) {
        binding.apply {
            tvSnackNamePlaceholder.text = historyData.snackName
            tvFatAmount.text = historyData.nutritions.fat.toString()
            tvSaturatedFatAmount.text = historyData.nutritions.saturatedFat.toString()
            tvCarbohydratesAmount.text = historyData.nutritions.carbohydrates.toString()
            tvSugarsAmount.text = historyData.nutritions.sugars.toString()
            tvFiberAmount.text = historyData.nutritions.fiber.toString()
            tvProteinsAmount.text = historyData.nutritions.protein.toString()
            tvSodiumAmount.text = historyData.nutritions.sodium.toString()

            val healthStatus = historyData.healthStatus
            val recommendation = historyData.recommendation

            tvPredictionResultPlaceholder.setTextColor(resources.getColor(R.color.md_theme_inversePrimary_highContrast))
            if (healthStatus == "Healthy") {
                binding.cvPredictionResult.setCardBackgroundColor(resources.getColor(R.color.card_color_green))
            } else {
                binding.cvPredictionResult.setCardBackgroundColor(resources.getColor(R.color.card_color_red))
            }
            binding.tvPredictionResultPlaceholder.text = healthStatus
            binding.tvRecommendationPlaceholder.text = recommendation

            val categories = historyData.categories

            setupCategories(categories)
        }
    }

    private fun setupObserver() {
        viewModel.historyDetail.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnReloadHistory.visibility = View.GONE
                    binding.tvNoHistory.visibility = View.GONE
                    binding.containerHistoryDetail.visibility = View.GONE
                }

                is ResultState.Success -> {
                    binding.tvNoHistory.visibility = View.GONE
                    binding.btnReloadHistory.visibility = View.GONE
                    binding.containerHistoryDetail.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    val historyData = result.data.data
                    setupUI(historyData)
                }

                is ResultState.Error -> {
                    binding.containerHistoryDetail.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    binding.btnReloadHistory.visibility = View.VISIBLE
                    binding.tvNoHistory.visibility = View.VISIBLE
                }
            }
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
                    ivCategoryFat.setColorFilter(resources.getColor(R.color.card_color_lime))
                }
                "3" -> {
                    ivCategoryFat.setImageResource(R.drawable.ic_moderate)
                    ivCategoryFat.setColorFilter(resources.getColor(R.color.md_theme_onPrimaryContainer))
                }
                "4" -> {
                    ivCategoryFat.setImageResource(R.drawable.ic_high)
                    ivCategoryFat.setColorFilter(resources.getColor(R.color.card_color_orange))
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
                    ivCategorySaturatedFat.setColorFilter(resources.getColor(R.color.card_color_lime))
                }
                "3" -> {
                    ivCategorySaturatedFat.setImageResource(R.drawable.ic_moderate)
                    ivCategorySaturatedFat.setColorFilter(resources.getColor(R.color.md_theme_onPrimaryContainer))
                }
                "4" -> {
                    ivCategorySaturatedFat.setImageResource(R.drawable.ic_high)
                    ivCategorySaturatedFat.setColorFilter(resources.getColor(R.color.card_color_orange))
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
                    ivCategorySugars.setColorFilter(resources.getColor(R.color.card_color_lime))
                }

                "3" -> {
                    ivCategorySugars.setImageResource(R.drawable.ic_moderate)
                    ivCategorySugars.setColorFilter(resources.getColor(R.color.md_theme_onPrimaryContainer))
                }

                "4" -> {
                    ivCategorySugars.setImageResource(R.drawable.ic_high)
                    ivCategorySugars.setColorFilter(resources.getColor(R.color.card_color_orange))
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
                    ivCategoryCarbohydrates.setColorFilter(resources.getColor(R.color.card_color_green))
                }
                "2" -> {
                    ivCategoryCarbohydrates.setImageResource(R.drawable.ic_low)
                    ivCategoryCarbohydrates.setColorFilter(resources.getColor(R.color.card_color_lime))
                }
                "3" -> {
                    ivCategoryCarbohydrates.setImageResource(R.drawable.ic_moderate)
                    ivCategoryCarbohydrates.setColorFilter(resources.getColor(R.color.md_theme_onPrimaryContainer))
                }
                "4" -> {
                    ivCategoryCarbohydrates.setImageResource(R.drawable.ic_high)
                    ivCategoryCarbohydrates.setColorFilter(resources.getColor(R.color.card_color_orange))
                }
                "5" -> {
                    ivCategoryCarbohydrates.setImageResource(R.drawable.ic_very_high)
                    ivCategoryCarbohydrates.setColorFilter(resources.getColor(R.color.card_color_red))
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
                    ivCategoryProtein.setColorFilter(resources.getColor(R.color.card_color_orange))
                }
                "3" -> {
                    ivCategoryProtein.setImageResource(R.drawable.ic_moderate)
                    ivCategoryProtein.setColorFilter(resources.getColor(R.color.md_theme_onPrimaryContainer))
                }
                "4" -> {
                    ivCategoryProtein.setImageResource(R.drawable.ic_high)
                    ivCategoryProtein.setColorFilter(resources.getColor(R.color.card_color_lime))
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
                    ivCategoryFiber.setColorFilter(resources.getColor(R.color.card_color_orange))
                }
                "3" -> {
                    ivCategoryFiber.setImageResource(R.drawable.ic_moderate)
                    ivCategoryFiber.setColorFilter(resources.getColor(R.color.md_theme_onPrimaryContainer))
                }
                "4" -> {
                    ivCategoryFiber.setImageResource(R.drawable.ic_high)
                    ivCategoryFiber.setColorFilter(resources.getColor(R.color.card_color_lime))
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
                    ivCategorySodium.setColorFilter(resources.getColor(R.color.card_color_lime))
                }

                "3" -> {
                    ivCategorySodium.setImageResource(R.drawable.ic_moderate)
                    ivCategorySodium.setColorFilter(resources.getColor(R.color.md_theme_onPrimaryContainer))
                }

                "4" -> {
                    ivCategorySodium.setImageResource(R.drawable.ic_high)
                    ivCategorySodium.setColorFilter(resources.getColor(R.color.card_color_orange))
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