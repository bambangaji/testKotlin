package com.example.newsappassessment.model

data class News(
    val title: String,
    val description: String,
    val url: String,
    val author: String,
    val source: NewsSource,
    val urlToImage: String,
    val publishedAt: String,
    val content:String
)

data class NewsList(
    val news: List<News>
)

data class NewsSource(
    val id:String,
    val name:String
)