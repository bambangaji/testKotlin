package com.example.newsappassessment.network

import com.example.newsappassessment.AppConfig
import com.example.newsappassessment.model.NewsList
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = AppConfig.BASE_URL
interface ApiService {

    @GET("top-headlines")
    suspend fun getNews(
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("apiKey") apiKey: String
    ): ResponseBody

    @GET("top-headlines")
    suspend fun getEverything(
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("apiKey") apiKey: String,
        @Query("q") q: String,
        @Query("pageSize") pageSize:Int,
        @Query("page") page:Int
    ): ResponseBody

    companion object {
        private val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        private val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()

        private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        fun create(): ApiService {
            return retrofit.create(ApiService::class.java)
        }
    }

}