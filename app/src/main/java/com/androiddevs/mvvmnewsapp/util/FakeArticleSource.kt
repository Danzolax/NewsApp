package com.androiddevs.mvvmnewsapp.util

import com.androiddevs.mvvmnewsapp.models.Article

object FakeArticleSource {

    fun generateArticles(text: String): MutableList<Article> {
        val articles = mutableListOf<Article>()
        for (i in 1..10) {
            articles.add(
                Article(
                    null,
                    "$text$i",
                    "$text$i",
                    "$text$i",
                    "$text$i",
                    null,
                    "$text$i",
                    "https://google.com",
                    ""
                )
            )
        }
        return articles
    }
}