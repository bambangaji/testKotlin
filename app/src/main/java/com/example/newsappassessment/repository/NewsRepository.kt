package com.example.newsappassessment.repository

import com.example.newsappassessment.model.News
import com.example.newsappassessment.model.NewsSource
import com.example.newsappassessment.network.ApiService
import org.json.JSONException
import org.json.JSONObject

class NewsRepository {
    private val apiService = ApiService.create()

    suspend fun getNews(category:String,page:Int,query:String): List<News> {
        val response = apiService.getEverything("ID", category, "44f7b3fb576e423fb46a251d80397b94",query, page = page, pageSize = 10,)
        val jsonResponse = response.string() // Get the raw JSON response as a string

        try {
            // Parse JSON response into JSONObject
            val newsList = JSONObject(jsonResponse)

            // Extract "articles" array from JSONObject
            val articlesArray = newsList.getJSONArray("articles")

            // Initialize a list to store parsed News objects
            val newsListOutput = mutableListOf<News>()

            // Iterate through the articles array and parse each article into a News object
            for (i in 0 until articlesArray.length()) {
                val articleObject = articlesArray.getJSONObject(i)
                val title = articleObject.getString("title")
                val description = articleObject.getString("description")
                val imageUrl = articleObject.getString("urlToImage")
                val url = articleObject.getString("url")
                val author = articleObject.getString("author")
                val source = articleObject.getJSONObject("source")
                val newsSource = NewsSource(source.getString("id"),source.getString("name"))
                val publishedAt = articleObject.getString("publishedAt")
                val content = articleObject.getString("content")
                val news = News(title, description,url,author,newsSource, imageUrl ,publishedAt,content )
                newsListOutput.add(news)
            }
    println("newss;;")
            println(newsListOutput)
            println(newsListOutput[2])
            // Return the list of parsed News objects
            return newsListOutput
        } catch (e: JSONException) {
            // Handle parsing error
            println("Error parsing JSON: ${e.message}")
        }

        // Return an empty list if there was an error parsing the JSON response
        return emptyList()
    }
}
