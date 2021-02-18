package com.androiddevs.mvvmnewsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.models.Article
import com.androiddevs.mvvmnewsapp.models.NewsResponse
import timber.log.Timber

class CategoryViewPagerAdapter(
    val newsAdapter: NewsAdapter
) :
    RecyclerView.Adapter<CategoryViewPagerAdapter.CategoryViewHolder>() {

    private val allArticles = MutableList<List<Article>>(6) { listOf() }

    class CategoryViewHolder(itemView: View, newsAdapter: NewsAdapter) :
        RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvCategoryName)
        val rv = itemView.findViewById<RecyclerView>(R.id.rvArticles).apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(itemView.context)
        }
    }

    private val callback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }
    private val differ = AsyncListDiffer(this, callback)

    var names: List<String>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_category_preview, parent, false
            ),
            newsAdapter
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val title = names[position]
        val articles = allArticles[position]
        Timber.d(articles.toString())
        newsAdapter.articles = articles
        holder.apply {
            name.text = title
        }
    }

    override fun getItemCount(): Int {
        return names.size
    }

    var listener: ((Article) -> Unit)? = null

    fun setBusinessArticles(newsResponse: NewsResponse) {
        allArticles[0] = newsResponse.articles
    }

    fun setEntertainmentArticles(newsResponse: NewsResponse) {
        allArticles[1] = newsResponse.articles
    }

    fun setHealthArticles(newsResponse: NewsResponse) {
        allArticles[2] = newsResponse.articles
    }

    fun setScienceArticles(newsResponse: NewsResponse) {
        allArticles[3] = newsResponse.articles
    }

    fun setSportsArticles(newsResponse: NewsResponse) {
        allArticles[4] = newsResponse.articles
    }

    fun setTechnologyArticles(newsResponse: NewsResponse) {
        allArticles[5] = newsResponse.articles
    }
}