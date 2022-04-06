package com.myapp.bestapptest.domain.repository

import com.myapp.bestapptest.domain.model.Article
import com.myapp.bestapptest.util.Resource
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getNews(): Flow<Resource<List<Article>>>

    suspend fun getNewsById(id: Int): Article
}