package com.androiddevs.mvvmnewsapp.models

data class NewsResponse(
    val articles: MutableList<Article> = mutableListOf(),
    val status: String = "",
    val totalResults: Int = 0
)