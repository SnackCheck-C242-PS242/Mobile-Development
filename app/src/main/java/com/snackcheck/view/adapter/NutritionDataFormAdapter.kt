package com.snackcheck.view.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.snackcheck.data.local.entity.NutritionItem
import com.snackcheck.databinding.ItemNutritionFormBinding

class NutritionDataFormAdapter :
    ListAdapter<NutritionItem, NutritionDataFormAdapter.NutritionViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NutritionItem>() {
            override fun areItemsTheSame(oldItem: NutritionItem, newItem: NutritionItem): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: NutritionItem, newItem: NutritionItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NutritionViewHolder {
        val binding = ItemNutritionFormBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return NutritionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NutritionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NutritionViewHolder(private val binding: ItemNutritionFormBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NutritionItem) {
            val dropdownItems = listOf("Protein", "Carbohydrate", "Fat", "Fiber")
            val adapter = ArrayAdapter(
                binding.root.context,
                android.R.layout.simple_dropdown_item_1line,
                dropdownItems
            )
            binding.tvAutoComplete.setAdapter(adapter)

            // Bind current item data
            binding.tvAutoComplete.setText(item.name, false) // false untuk menghindari auto-callback
            binding.etInputNutrition.setText(item.amount)

            // Handle dropdown selection
            binding.tvAutoComplete.setOnItemClickListener { _, _, position, _ ->
                item.name = dropdownItems[position]
            }

            // Handle text input
            binding.etInputNutrition.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    item.amount = s.toString()
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
    }
}