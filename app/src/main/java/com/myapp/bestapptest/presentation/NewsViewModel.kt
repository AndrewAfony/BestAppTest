package com.myapp.bestapptest.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.bestapptest.domain.model.Article
import com.myapp.bestapptest.domain.repository.NewsRepository
import com.myapp.bestapptest.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository
): ViewModel() {

    var news = MutableLiveData<List<Article>>()
    private set

    var showSplash = true

    fun getNews() {
        viewModelScope.launch {
            repository.getNews()
                .onEach { result ->
                    when(result) {
                        is Resource.Success -> {
                            result.data?.let {
                                news.postValue(it)
                            }
                            showSplash = false
                        }
                        is Resource.Error -> {
                            showSplash = false
                        }
                        is Resource.Loading -> {
                            showSplash = true
                        }
                    }

                }.launchIn(this)
        }
    }

    fun getNewsById(id: Int) {
        viewModelScope.launch {
            repository.getNewsById(id)
        }
    }

}