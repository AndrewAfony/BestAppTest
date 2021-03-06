package com.myapp.bestapptest.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myapp.bestapptest.R
import com.myapp.bestapptest.databinding.NewsItemBinding
import com.myapp.bestapptest.domain.model.Article
import com.myapp.bestapptest.util.toDate

class NewsAdapter(
    val onClick: (Int) -> Unit
): RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(val binding: NewsItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(
            NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    private val differCallback = object :DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.binding.apply {
            title.text = article.title
            description.text = article.description
            publishedAt.text = article.publishedAt.toDate()
            Glide.with(this.root)
                .load(article.urlToImage)
                .error(R.drawable.ic_error)
                .into(image)

            this.root.setOnClickListener {
                onClick(article.id)
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size
}