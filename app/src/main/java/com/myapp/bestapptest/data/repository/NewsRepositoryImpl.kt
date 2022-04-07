package com.myapp.bestapptest.data.repository

import com.myapp.bestapptest.data.local.NewsDatabase
import com.myapp.bestapptest.data.remote.NewsApi
import com.myapp.bestapptest.domain.model.Article
import com.myapp.bestapptest.domain.repository.NewsRepository
import com.myapp.bestapptest.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val api: NewsApi,
    private val db: NewsDatabase
): NewsRepository {

    override fun getNews(): Flow<Resource<List<Article>>> = flow {

        emit(Resource.Loading())

        try {
            val news = api.getNews()
            db.dao.deleteNews()
            db.dao.insertNews(news.articles)
            emit(Resource.Success())
        } catch (e: HttpException) {
            emit(Resource.Error(message = e.localizedMessage ?: "Unknown error"))
        } catch (e: IOException) {
            emit(Resource.Error(message = e.localizedMessage ?: "Unknown error"))
        }
    }

    override fun getSavedNews(): Flow<List<Article>> {
        return db.dao.getAllNews()
    }

    override fun getSearchedNews(query: String): Flow<List<Article>> {
        return db.dao.getSearchedNews(query)
    }

    override suspend fun getNewsById(id: Int): Article {
        return db.dao.getNewsById(id)
    }
}