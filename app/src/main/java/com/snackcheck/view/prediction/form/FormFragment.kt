package com.snackcheck.view.prediction.form

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.snackcheck.R
import com.snackcheck.databinding.FragmentFormBinding

class FormFragment : Fragment() {

    private var _binding: FragmentFormBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentFormBinding.bind(view)

        val nutrition = listOf("Fat", "Protein", "Carbohydrate", "Fiber", "Sugar")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, nutrition)
        binding.tvAutoComplete.setAdapter(adapter)
        binding.tvAutoComplete.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            binding.tvAutoComplete.setText(selectedItem)
        }
    }
}