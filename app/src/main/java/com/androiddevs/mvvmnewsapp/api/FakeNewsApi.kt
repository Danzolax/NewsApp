package com.androiddevs.mvvmnewsapp.api

import com.androiddevs.mvvmnewsapp.api.NewsApi
import com.androiddevs.mvvmnewsapp.models.Article
import com.androiddevs.mvvmnewsapp.models.NewsResponse
import com.androiddevs.mvvmnewsapp.util.FakeArticleSource.generateArticles
import retrofit2.Response
import timber.log.Timber

class FakeNewsApi : NewsApi {
    override suspend fun getBreakingNews(
        countryCode: String,
        page: Int,
        apiKey: String
    ): Response<NewsResponse> {
        return Response.success(
            NewsResponse()
        )
    }

    override suspend fun searchForNews(
        searchQuery: String,
        page: Int,
        apiKey: String
    ): Response<NewsResponse> {
        return Response.success(
            NewsResponse()
        )
    }

    override suspend fun getNewsByCategory(
        category: String,
        page: Int,
        apiKey: String
    ): Response<NewsResponse> {
        val articles = generateArticles(category)
        Timber.d(articles.toString())
        return Response.success(NewsResponse(articles, "OK", articles.size))
    }
}