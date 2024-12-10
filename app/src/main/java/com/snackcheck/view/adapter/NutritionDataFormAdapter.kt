package com.snackcheck.view.adapter

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.snackcheck.R
import com.snackcheck.data.local.entity.NutritionItem
import com.snackcheck.databinding.ItemNutritionFormBinding
import com.snackcheck.view.prediction.form.FormViewModel

class NutritionDataFormAdapter(private val viewModel: FormViewModel) :
    ListAdapter<NutritionItem, NutritionDataFormAdapter.NutritionViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NutritionItem>() {
            override fun areItemsTheSame(oldItem: NutritionItem, newItem: NutritionItem): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: NutritionItem,
                newItem: NutritionItem,
            ): Boolean {
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

        private var previousSelection: String? = null


        fun bind(item: NutritionItem) {
            // Observasi perubahan status nutrisi
            viewModel.nutrientStatus.observe((binding.root.context as LifecycleOwner)) { statusMap ->

                val availableNutrients = statusMap.filterValues { it == 0 }.keys.toMutableList()

                // Jika item.name sudah dipilih, tambahkan ke daftar dropdown
                if (item.name.isNotEmpty() && !availableNutrients.contains(item.name)) {
                    Log.d("NutritionDataFormAdapter", "Item.name: ${item.name}")
                    availableNutrients.add(0, item.name)
                }

                val adapter = ArrayAdapter(
                    binding.root.context,
                    android.R.layout.simple_dropdown_item_1line,
                    availableNutrients
                )
                binding.tvAutoComplete.setAdapter(adapter)

                // Tetapkan nilai dropdown berdasarkan item.name
                binding.tvAutoComplete.setText(item.name, false)
            }


            // Tangani pemilihan dropdown
            binding.tvAutoComplete.setOnItemClickListener { _, _, pos, _ ->
                val selected = (binding.tvAutoComplete.adapter.getItem(pos) as String)

                // Perbarui status nutrisi di ViewModel
                viewModel.updateNutrientStatus(selected, previousSelection)

                // Perbarui nilai item
                previousSelection = selected
                item.name = selected

                Log.d("NutritionDataFormAdapter", "Selected: $selected")
                Log.d("NutritionDataFormAdapter", "Previous Selection: $previousSelection")

                // Tetapkan nilai dropdown dengan segera
                if (selected == binding.root.context.getString(R.string.sodium)) {
                    binding.edNutritionAmount.setText(binding.root.context.getString(R.string.milligram))
                } else {
                    binding.edNutritionAmount.setText(binding.root.context.getString(R.string.gram))
                }

                if (selected == binding.root.context.getString(R.string.carbohydrates) || selected == binding.root.context.getString(R.string.saturated_fat)) {
                    binding.tvAutoComplete.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                } else {
                    binding.tvAutoComplete.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                }


                binding.tvAutoComplete.setText(selected, false)
            }


            // Tangani input jumlah nutrisi
            binding.edInputNutrition.setText(item.amount)
            binding.edInputNutrition.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    item.amount = s.toString()
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
    }
}