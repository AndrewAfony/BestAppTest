package com.myapp.bestapptest.data.remote

import com.myapp.bestapptest.data.dto.Response
import com.myapp.bestapptest.domain.model.Article
import retrofit2.http.GET

interface NewsApi {

    @GET("/NewsAPI/top-headlines/category/science/in.json")
    suspend fun getNews(): Response
}