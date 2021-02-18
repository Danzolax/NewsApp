package com.androiddevs.mvvmnewsapp.repository

import com.androiddevs.mvvmnewsapp.api.NewsApi
import com.androiddevs.mvvmnewsapp.db.ArticleDao
import com.androiddevs.mvvmnewsapp.models.Article
import javax.inject.Inject

class NewsRepository @Inject constructor(
    val dao: ArticleDao,
    val api: NewsApi
) {
    suspend fun getBreakingNews(
        countryCode: String
    ) = api.getBreakingNews(countryCode)

    suspend fun searchNews(
        searchQuery: String
    ) = api.searchForNews(searchQuery)

    suspend fun upsert(article: Article) = dao.upsert(article)

    fun getSavedNews() = dao.getAllArticles()

    suspend fun deleteArticle(article: Article) = dao.delete(article)

    suspend fun getNewsByCategory(
        category: String
    ) = api.getNewsByCategory(category)
}
