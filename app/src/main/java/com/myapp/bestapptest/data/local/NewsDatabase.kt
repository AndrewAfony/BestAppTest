package com.myapp.bestapptest.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.myapp.bestapptest.domain.model.Article

@Database(entities = [Article::class], version = 1)
abstract class NewsDatabase: RoomDatabase() {

    abstract val dao: NewsDao
}