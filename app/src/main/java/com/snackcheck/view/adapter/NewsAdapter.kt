package com.snackcheck.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.snackcheck.data.remote.model.ArticlesItem
import com.snackcheck.databinding.ItemNewsBinding

class NewsAdapter(private val articles: List<ArticlesItem>) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ArticlesItem) {
            binding.tvNewsTitle.text = article.title
            binding.tvNewsDescription.text = article.description
            Glide.with(itemView.context)
                .load(article.urlToImage)
                .into(binding.ivNewsPicture)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = articles[position]
        holder.bind(article)
    }

    override fun getItemCount(): Int = articles.size
}