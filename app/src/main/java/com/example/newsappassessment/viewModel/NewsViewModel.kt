package com.example.newsappassessment.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsappassessment.model.News
import com.example.newsappassessment.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {
    private val newsRepository = NewsRepository()
    private val _news = MutableLiveData<List<News>>() // Using MutableLiveData to hold news data
    private val _articleDetail = MutableLiveData<News>() // Using MutableLiveData to hold news detail
    val news: LiveData<List<News>> get() = _news
    val articleDetail: LiveData<News> get() = _articleDetail // Expose articleDetail as LiveData

    init {
        fetchNews("Business",1)
    }

    private fun fetchNews(category: String, page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val newsData = newsRepository.getNews(category,page,"")
                _news.postValue(newsData)
            } catch (e: Exception) {
                // Handle errors
                e.printStackTrace()
            }
        }
    }

    // Function to set the selected news detail
    fun setArticleDetail(news: News) {
        println("article;;")
        println(news)
        _articleDetail.postValue(news)

        println(articleDetail.toString())
    }
}