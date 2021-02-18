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
        countryCode: String, pageNumber: Int
    ) = api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(
        searchQuery: String, pageNumber: Int
    ) = api.searchForNews(searchQuery, pageNumber)

    suspend fun upsert(article: Article) = dao.upsert(article)

    fun getSavedNews() = dao.getAllArticles()

    suspend fun deleteArticle(article: Article) = dao.delete(article)

    suspend fun getNewsBySources(
        sources: String
    ) = api.getNewsBySources(sources = sources)
}
