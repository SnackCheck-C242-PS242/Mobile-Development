package com.snackcheck.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.snackcheck.R
import com.snackcheck.helper.withDateFormat
import com.snackcheck.data.remote.model.HistoryData
import com.snackcheck.databinding.ItemHistoryBinding

class HistoryAdapter(private val onItemClicked: (HistoryData) -> Unit) :
    ListAdapter<HistoryData, HistoryAdapter.HistoryViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): HistoryAdapter.HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryAdapter.HistoryViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history)
    }

    inner class HistoryViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(history: HistoryData) {
            binding.tvSnackName.text = history.snackName
            binding.tvResult.text = history.healthStatus
            binding.tvTimeCreated.text = history.createdAt.withDateFormat()


            val healthStatus = history.healthStatus
            if (healthStatus == "Healthy") {
                binding.cvHealthStatus.setCardBackgroundColor(
                    ContextCompat.getColor(binding.root.context, R.color.card_color_green)
                )
            } else {
                binding.cvHealthStatus.setCardBackgroundColor(
                    ContextCompat.getColor(binding.root.context, R.color.card_color_red)
                )
            }

            binding.tvResult.setTextColor(binding.root.context.getColor(R.color.color_white))

            binding.root.setOnClickListener {
                onItemClicked(history)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoryData>() {
            override fun areItemsTheSame(oldItem: HistoryData, newItem: HistoryData): Boolean {
                return oldItem.snackName == newItem.snackName
            }

            override fun areContentsTheSame(oldItem: HistoryData, newItem: HistoryData): Boolean {
                return oldItem == newItem
            }
        }
    }


}