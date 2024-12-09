package com.snackcheck.view.history.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.snackcheck.R
import com.snackcheck.data.ResultState
import com.snackcheck.data.pref.UserPreference
import com.snackcheck.data.pref.dataStore
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
            tvProteinsAmount.text = historyData.nutritions.proteins.toString()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}