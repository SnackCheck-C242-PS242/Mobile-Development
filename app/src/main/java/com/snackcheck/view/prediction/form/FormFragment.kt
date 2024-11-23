package com.snackcheck.view.prediction.form

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.snackcheck.databinding.FragmentFormBinding
import com.snackcheck.view.adapter.NutritionDataFormAdapter

class FormFragment : Fragment() {

    private var _binding: FragmentFormBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FormViewModel by viewModels()
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

        // Setup RecyclerView
        adapter = NutritionDataFormAdapter()
        binding.dynamicContainer.layoutManager = LinearLayoutManager(requireContext())
        binding.dynamicContainer.adapter = adapter

        // Observe ViewModel
        viewModel.nutritionData.observe(viewLifecycleOwner) { data ->
            adapter.submitList(data)
        }

        // Button Plus Click
        binding.btnPlus.setOnClickListener {
            viewModel.addEmptyNutritionItem()
        }

        binding.btnMin.setOnClickListener {
            binding.btnMin.setOnClickListener {
                if (viewModel.nutritionData.value.orEmpty().size > 1) {
                    viewModel.removeLastNutritionItem() // Hapus elemen terakhir
                } else {
                    Toast.makeText(requireContext(), "Tidak bisa menghapus elemen terakhir.", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}