package com.myapp.bestapptest.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.bestapptest.domain.model.Article
import com.myapp.bestapptest.domain.repository.NewsRepository
import com.myapp.bestapptest.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository
): ViewModel() {

    var news = MutableStateFlow<List<Article>>(emptyList())
    private set

    var currentArticle = MutableLiveData<Article>()
    private set

    var eventFlow = MutableSharedFlow<String>()
    private set

    var showSplash = true

    init {
        getNews()
    }

    private fun getNews() {
        viewModelScope.launch {
            repository.getNews()
                .onEach { result ->
                    when(result) {
                        is Resource.Success -> {
                            repository.getSavedNews()
                                .collect {
                                    news.emit(it)
                                    delay(50L)
                                    showSplash = false
                                }
                        }
                        is Resource.Error -> {
                            showSplash = false
                            eventFlow.emit(result.message ?: "")
                        }
                        is Resource.Loading -> {
                            showSplash = true
                        }
                    }
                }.launchIn(this)
        }
    }

    fun onFilter(query: String) {
        viewModelScope.launch {
            repository.getSearchedNews(query)
                .flowOn(Dispatchers.IO)
                .collectLatest {
                news.emit(it)
            }
        }
    }

    fun getNewsById(id: Int) {
        viewModelScope.launch {
            val res = repository.getNewsById(id)
            currentArticle.postValue(res)
        }
    }
}