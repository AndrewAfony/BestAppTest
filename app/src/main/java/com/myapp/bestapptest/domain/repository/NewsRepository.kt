package com.myapp.bestapptest.domain.repository

import com.myapp.bestapptest.domain.model.Article
import com.myapp.bestapptest.util.Resource
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getNews(): Flow<Resource<List<Article>>>

    fun getSearchedNews(query: String): Flow<List<Article>>

    fun getSavedNews(): Flow<List<Article>>

    suspend fun getNewsById(id: Int): Article
}