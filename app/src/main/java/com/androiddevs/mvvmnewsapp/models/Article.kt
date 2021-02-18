package com.androiddevs.mvvmnewsapp.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(
    tableName = "articles"
)
data class Article(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val author: String = "",
    val content: String = "",
    val description: String = "",
    val publishedAt: String = "",
    val source: Source? = null,
    val title: String = "",
    val url: String = "",
    val urlToImage: String?
) : Serializable {
    override fun toString(): String {
        return "$author - $title"
    }
}