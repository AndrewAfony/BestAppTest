package com.myapp.bestapptest.data.local

import androidx.room.*
import com.myapp.bestapptest.domain.model.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Query("SELECT * FROM Article")
    fun getAllNews(): Flow<List<Article>>

    @Query("SELECT * FROM Article WHERE id=:id")
    suspend fun getNewsById(id: Int): Article

    @Query("DELETE FROM Article WHERE id IN (:news)")
    suspend fun deleteNews(news: List<Int>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: List<Article>)

}