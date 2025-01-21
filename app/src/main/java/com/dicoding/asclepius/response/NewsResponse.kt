package com.dicoding.asclepius.response

import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @field:SerializedName("status")
    val status: String,
    @field:SerializedName("totalResults")
    val totalResults: Int,
    @field:SerializedName("articles")
    val articles: List<Article>
)

data class Article(
    @field:SerializedName("title")
    val title: String,
    @field:SerializedName("description")
    val description: String?,
    @field:SerializedName("url")
    val url: String,
    @field:SerializedName("urlToImage")
    val urlToImage: String?
)