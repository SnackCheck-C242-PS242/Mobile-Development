package com.snackcheck.view.prediction.form

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.snackcheck.R
import com.snackcheck.data.ResultState
import com.snackcheck.data.pref.UserPreference
import com.snackcheck.data.pref.dataStore
import com.snackcheck.databinding.FragmentFormBinding
import com.snackcheck.view.ViewModelFactory
import com.snackcheck.view.adapter.NutritionDataFormAdapter

class FormFragment : Fragment() {
    private var _binding: FragmentFormBinding? = null
    private val binding get() = _binding!!

    private lateinit var pref: UserPreference
    private lateinit var factory: ViewModelFactory
    private val viewModel by viewModels<FormViewModel> { factory }
    private lateinit var adapter: NutritionDataFormAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = UserPreference.getInstance(requireContext().dataStore)
        factory = ViewModelFactory.getInstance(requireContext(), pref)

        // Setup RecyclerView
        adapter = NutritionDataFormAdapter(viewModel)
        binding.dynamicContainer.layoutManager = LinearLayoutManager(requireContext())
        binding.dynamicContainer.adapter = adapter

        // Observe ViewModel
        viewModel.nutritionData.observe(viewLifecycleOwner) { data ->
            adapter.submitList(data)
        }

        // Button Plus Click
        viewModel.isAddButtonVisible.observe(viewLifecycleOwner) { isVisible ->
            binding.btnPlus.visibility = if (isVisible) View.VISIBLE else View.GONE
        }

        setupAction()
    }

    private fun setupAction() {
        binding.apply {
            btnPlus.setOnClickListener {
                viewModel.addEmptyNutritionItem()
            }

            btnMin.setOnClickListener {
                val currentSize = viewModel.nutritionData.value.orEmpty().size
                if (currentSize > 1) {
                    viewModel.removeLastNutritionItem()
                    adapter.notifyItemRemoved(currentSize - 1)
                } else {
                    Toast.makeText(requireContext(), "Failed to remove.", Toast.LENGTH_SHORT).show()
                }
            }

            btnAnalyze.setOnClickListener {
                val snackName = binding.etSnackName.text.toString()
                if (snackName.isEmpty()) {
                    Toast.makeText(requireContext(), "Snack name cannot be empty.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val snackDetail = viewModel.getSnackDetailFromInput(snackName)

                val builder: AlertDialog.Builder =
                    MaterialAlertDialogBuilder(this@FormFragment.requireContext(), R.style.MaterialAlertDialog_Rounded)
                builder.setView(R.layout.layout_loading)
                val dialog: AlertDialog = builder.create()

                viewModel.predictSnack(snackDetail)
                viewModel.responseResult.observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is ResultState.Loading -> dialog.show()
                        is ResultState.Success -> {
                            dialog.dismiss()
                            val snackPredictResponse = result.data
                            val bundle = Bundle().apply {
                                putParcelable("snackPredictResponse", snackPredictResponse)
                            }
                            findNavController().navigate(R.id.navigation_result, bundle)
                        }
                        is ResultState.Error -> {
                            dialog.dismiss()
                            Toast.makeText(
                                requireContext(),
                                result.error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else -> dialog.dismiss()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}