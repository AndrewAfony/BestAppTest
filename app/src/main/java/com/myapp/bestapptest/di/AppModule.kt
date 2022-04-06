package com.myapp.bestapptest.di

import android.content.Context
import androidx.room.Room
import com.myapp.bestapptest.BuildConfig
import com.myapp.bestapptest.data.local.NewsDatabase
import com.myapp.bestapptest.data.remote.NewsApi
import com.myapp.bestapptest.data.repository.NewsRepositoryImpl
import com.myapp.bestapptest.domain.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNewsApi(): NewsApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .build()
            .create(NewsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsRepository(api: NewsApi, db: NewsDatabase): NewsRepository {
        return NewsRepositoryImpl(api, db)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NewsDatabase {
        return Room.databaseBuilder(
            context,
            NewsDatabase::class.java,
            "news_db"
        ).build()
    }
}